package top.readm.demo.dhtnetwork.bt.tracker.client;

import top.readm.demo.dhtnetwork.bt.tracker.TrackerParam;
import top.readm.demo.dhtnetwork.bt.tracker.TrackerResponse;

import java.util.concurrent.Future;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/22
 */
public interface TrackerClient {

    Future<TrackerResponse> tracker(String trackerUrl, TrackerParam request);

}
