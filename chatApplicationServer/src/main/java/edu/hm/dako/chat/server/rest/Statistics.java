/**
 * Organisation: University of Applied Sciences Munich, FK07
 * Project: Software-Architektur Praktikum
 * Sub-Project: chatApplication
 * Author: David Sautter (04358212)
 * Created On: 29.11.16
 * Operating-System: Arch-Linux
 * Java-Version: 1.8.0_40
 * CPU: Intel(R) Core(TM)2 Duo CPU T5870 @ 2.00GHz
 */

package edu.hm.dako.chat.server.rest;

import edu.hm.dako.chat.server.datasink.DataSink;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;
import edu.hm.dako.chat.server.user.SharedChatClientList;

import javax.inject.Inject;
import java.util.List;
import java.util.OptionalDouble;

class Statistics {

    @Inject
    private DataSink dataSink;
    private SharedChatClientList sharedChatClientList;

    private List<TraceEntity> traceData;

    long numClients;
    int totalMessages;
    OptionalDouble avgMsgLength;

    Statistics() {
        sharedChatClientList = SharedChatClientList.getInstance();
        numClients = sharedChatClientList.size();
        traceData = dataSink.getAllTraceData();
        totalMessages = traceData.size();

        if (totalMessages > 0) {
            avgMsgLength = traceData.parallelStream()
                    .mapToInt(trace -> trace.getNachricht().length())
                    .average();
        }
    }
}
