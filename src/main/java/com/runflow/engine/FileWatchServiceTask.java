package com.runflow.engine;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileWatchServiceTask implements Runnable {

    private String rootDir;

    public FileWatchServiceTask(String rootDir) {
        this.rootDir = rootDir;
    }

    ExecutorService cachedThreadPool = Executors.newFixedThreadPool(5);

    @Override
    public void run() {
        // 获取当前文件系统的监控对象
        try(WatchService service = FileSystems.getDefault().newWatchService()){
            //获取文件目录下的Path对象注册到 watchService中, 监听的事件类型，有创建，删除，以及修改
            Paths.get(rootDir).register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                //获取可用key.没有可用的就wait
                WatchKey key = service.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    // TODO
//                    log.info(event.context() + "文件:" + event.kind() + "次数: " + event.count());
                }
                // 重置，这一步很重要，否则当前的key就不再会获取将来发生的事件
                boolean valid = key.reset();
                // 失效状态，退出监听
                if (!valid) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
//            log.error("Encountered exception when try to get new WatchService : {}", e.fillInStackTrace());
            Thread.currentThread().interrupt();
        }
    }
}
