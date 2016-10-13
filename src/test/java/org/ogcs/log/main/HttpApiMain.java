package org.ogcs.log.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.config.OkraConfig;
import org.ogcs.log.config.OkraProperties;
import org.ogcs.log.core.MissionBoard;
import org.ogcs.log.core.server.HttpLogServer;
import org.ogcs.log.serlvet.ApiHandler;
import org.ogcs.log.serlvet.ApiServer;
import org.ogcs.log.serlvet.grafana.GrafanaUtil;
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
        ApiServer apiServer = null;
        try {
            OkraConfig config = OkraProperties.getConfig();
            MissionBoard missionBoard = new MissionBoard(config);
            missionBoard.init();

            server = new HttpLogServer(config.getPort(), missionBoard);
            server.start();

            ApiHandler.register("/api.action", new AdminServlet());
            GrafanaUtil.register();

            apiServer = new ApiServer(9006);
            apiServer.start();
            LOG.info("Okra-LOG bootstrap success.");
        } catch (Exception e) {
            if (server != null)
                server.stop();
            LOG.info("Okra-LOG bootstrap failure.", e);
        }
    }
}
