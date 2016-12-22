package org.saur.initilaiser;


import java.io.File;

public interface Initialiser {

    File prepareEnvironment();

    void writeRootRouterClass(File projRootDir);

    void writePingResourceClass(File projectRootDir);

    void writeAPI();
}
