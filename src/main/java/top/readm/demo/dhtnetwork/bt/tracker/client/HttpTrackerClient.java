package top.readm.demo.dhtnetwork.bt.tracker.client;

import com.sun.xml.internal.ws.util.CompletedFuture;
import top.readm.demo.dhtnetwork.bencode.BDictionary;
import top.readm.demo.dhtnetwork.bt.tracker.TrackerParam;
import top.readm.demo.dhtnetwork.bt.tracker.TrackerResponse;
import top.readm.demo.dhtnetwork.bt.tracker.TrackerResponseParser;
import top.readm.demo.dhtnetwork.util.IOUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/22
 */
public class HttpTrackerClient implements TrackerClient {

    private TrackerResponseParser responseParser = new TrackerResponseParser();

    @Override
    public Future<TrackerResponse> tracker(String trackerUrl, TrackerParam request) {
        try{
            /**
             * 1. 构建url参数
             */
            String query = "?";
            query+="info_hash="+ request.getInfoHash();
            query+="&peer_id="+ request.getPeerId();
            query+="&port="+ String.valueOf(request.getPort());
            query+="&event="+ request.getEvent();
            /**
             * 2. 访问URL
             */
            URL url = new URL(trackerUrl + query);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("UserAgent","BitLet.org/0.1");
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            System.out.println(urlConnection.getURL());
            int respCode  =urlConnection.getResponseCode();
            /**
             * 3. 如果请求正常
             */
            try(InputStream in = new BufferedInputStream(urlConnection.getInputStream());){
                if(respCode == 302){
                    //重定向处理
                    return handleRedirects(urlConnection);
                }
                /**
                 * 这块要独立不出去，不要影响正常的IO
                 */
                BDictionary bDictionary = new BDictionary();
                bDictionary.decode(in);
                return new CompletedFuture<>(responseParser.parse(bDictionary), null);
            }
            catch (IOException ex){
                ex.printStackTrace();
                /**
                 * 4. 异常处理
                 */
                try(InputStream err = urlConnection.getErrorStream()){
                    String e = IOUtil.readAsString(err);
                    System.out.println("错误信息：");
                    System.out.println(e);
                }
                catch (Exception e){}
                throw new RuntimeException(ex);
            }
        }
        catch (Exception ex){
            return new CompletedFuture<>(null, ex);
        }
    }

    private CompletedFuture<TrackerResponse> handleRedirects(HttpURLConnection con) throws Exception{
        String location = con.getHeaderField("Location");
        con.disconnect();
        String url  = location;
        System.out.println("正在重定向"+url);

        try{
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            con.setRequestProperty("UserAgent","BitLet.org/0.1");
            con.setRequestMethod("GET");
            con.setConnectTimeout(30000);
            con.setReadTimeout(30000);
            con.setUseCaches(false);
            //System.out.println(url);
            try(InputStream in = con.getInputStream();){
                String s = IOUtil.readAsString(in);
                BDictionary bDictionary = new BDictionary();
                bDictionary.decode(new ByteArrayInputStream(s.getBytes()));
                return new CompletedFuture<>(responseParser.parse(bDictionary), null);
            }
            catch (IOException ex){
                try(InputStream err = con.getErrorStream()){
                    String e = IOUtil.readAsString(err);
                    System.out.println("重定向后错误信息：");
                    System.out.println(e);
                }
                throw new RuntimeException(ex);
            }
        }
        catch (Exception ex){
            return new CompletedFuture<>(null, ex);
        }
    }
}
