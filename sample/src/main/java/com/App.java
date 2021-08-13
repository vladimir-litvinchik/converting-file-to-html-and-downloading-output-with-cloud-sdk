package com;

import com.groupdocs.cloud.viewer.client.*;
import com.groupdocs.cloud.viewer.model.*;
import com.groupdocs.cloud.viewer.model.requests.CreateViewRequest;
import com.groupdocs.cloud.viewer.model.requests.DownloadFileRequest;
import com.groupdocs.cloud.viewer.model.requests.UploadFileRequest;
import com.groupdocs.cloud.viewer.api.FileApi;
import com.groupdocs.cloud.viewer.api.ViewApi;

import java.io.File;

public class App extends Thread  {

    public static void main(String[] args) throws Exception {
        String appSid = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
        String appKey = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        
        Configuration configuration = new Configuration(appSid, appKey);
        FileApi fileApi = new FileApi(configuration);
        ViewApi viewApi = new ViewApi(configuration);
        String filePath = "sample.dwg";

        // Upload a file to storage
        File file = new File(filePath);
        fileApi.uploadFile(new UploadFileRequest(filePath, file, null));

        // Convert a file to HTML
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilePath(filePath);

        ViewOptions viewOptions = new ViewOptions();
        viewOptions.setFileInfo(fileInfo);
        viewOptions.setViewFormat(ViewOptions.ViewFormatEnum.HTML);

        CreateViewRequest request = new CreateViewRequest();
        request.setviewOptions(viewOptions);

        ViewResult viewResult = viewApi.createView(request);

        // Download pages
        for (PageView pageView : viewResult.getPages()) {
            System.out.println("Page: " + pageView.getNumber() + " Path in storage: " + pageView.getPath());

            DownloadFileRequest dlRequest = new DownloadFileRequest();
            dlRequest.setpath(pageView.getPath());
            File srcFile = fileApi.downloadFile(dlRequest);
            File dstFile = new File("page_" + pageView.getNumber() + ".html");

            srcFile.renameTo(dstFile);

            System.out.println("Saved page at: " + dstFile);
        }
    }
}