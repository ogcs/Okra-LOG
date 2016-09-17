package org.ogcs.log.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.config.OkraConfig;
import org.ogcs.log.config.OkraProperties;
import org.ogcs.log.core.MissionBoard;
import org.ogcs.log.core.server.HttpLogServer;
import org.ogcs.log.serlvet.ApiHandler;
import org.ogcs.log.serlvet.impl.AdminServlet;

/**
 * @author TinyZ
 * @date 2016-08-01.
 */
public class HttpApiMain {
    private static final Logger LOG = LogManager.getLogger(HttpApiMain.class);

    public static void main(String[] args) {
        LOG.info("Bootstrap Okra-LOG ...");
        HttpLogServer server = null;
        try {
            OkraConfig config = OkraProperties.getConfig();
            MissionBoard missionBoard = new MissionBoard(config);
            missionBoard.init();

            ApiHandler.register("/api.action", new AdminServlet());

            server = new HttpLogServer(config.getPort(), missionBoard);
            server.start();
            LOG.info("Okra-LOG bootstrap success.");
        } catch (Exception e) {
            if (server != null)
                server.stop();
            LOG.info("Okra-LOG bootstrap failure.", e);
        }
    }
}
