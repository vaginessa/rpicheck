package de.eidottermihi.rpicheck.test;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.eidottermihi.rpicheck.ssh.beans.DiskUsageBean;
import de.eidottermihi.rpicheck.ssh.impl.RaspiQuery;
import de.eidottermihi.rpicheck.ssh.impl.RaspiQueryException;

public class IntegrationTest {

    private SshServer sshServer;

    @Before
    public void setupServer() {
        sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(22);
        sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());

        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
        userAuthFactories.add(new UserAuthPasswordFactory());
        sshServer.setUserAuthFactories(userAuthFactories);

        sshServer.setPasswordAuthenticator(new PasswordAuthenticator() {
            @Override
            public boolean authenticate(String username, String password, ServerSession session) throws PasswordChangeRequiredException {
                return username.equals("user") && password.equals("password");
            }
        });

        sshServer.setCommandFactory(new CommandFactory() {
            @Override
            public Command createCommand(String command) {
                if ("LC_ALL=C df -h".equals(command)) {
                    try {
                        return new OutputStreamSshCommand(new File("src/test/resources/disk_usage.txt"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });

        try {
            sshServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @After
    public void tearDownServer() throws IOException {
        sshServer.stop();
    }

    @Test
    public void test() throws RaspiQueryException {
        RaspiQuery query = new RaspiQuery("localhost", "user", 22);
        query.connect("password");

        final List<DiskUsageBean> diskUsageBeen = query.queryDiskUsage();
        Assert.assertNotNull(diskUsageBeen);
        Assert.assertEquals(8, diskUsageBeen.size());
    }
}
