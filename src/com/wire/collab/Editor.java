/**
 * This code was taken from:  https://github.com/pH-7/Simple-Java-Text-Editor
 *
 * @name Simple Java NotePad
 * @package ph.notepad
 * @file UI.java
 * @author SORIA Pierre-Henry
 * @email pierrehs@hotmail.com
 * @link http://github.com/pH-7
 * @copyright Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create 2012-05-04
 * @update 2016-21-03
 * @modifiedby Achintha Gunasekara
 * @modweb http://www.achinthagunasekara.com
 * @modemail contact@achinthagunasekara.com
 */

package com.wire.collab;

import com.wire.bots.sdk.*;
import com.wire.bots.sdk.server.model.Conversation;
import com.wire.bots.sdk.server.model.User;
import com.wire.bots.sdk.server.resources.MessageResource;
import com.wire.bots.sdk.user.Endpoint;
import com.wire.bots.sdk.user.UserClient;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class was taken from: https://github.com/pH-7/Simple-Java-Text-Editor
 */

public class Editor extends JTextPane {

    final static String AUTHOR_EMAIL = "pierrehenrysoria@gmail.com";
    final static String NAME = "PHNotePad";
    final static String EDITOR_EMAIL = "contact@achinthagunasekara.com";
    final static double VERSION = 3.0;
    private static final long serialVersionUID = 1L;
    private static final String CRYPTO_DIR = "data";

    public static void main(String[] args) throws Exception {
        String email = System.getProperty("email");
        String password = System.getProperty("password");
        String conv = System.getProperty("conv");

        Configuration config = new Configuration();
        config.cryptoDir = CRYPTO_DIR;

        WireClientFactory userClientFactory = (botId, convId, clientId, token) -> {
            String path = String.format("%s/%s", CRYPTO_DIR, botId);
            OtrManager otrManager = new OtrManager(path);
            return new UserClient(otrManager, botId, convId, clientId, token);
        };
        ClientRepo repo = new ClientRepo(userClientFactory, CRYPTO_DIR);
        OTMessageHandler handler = new OTMessageHandler();

        MessageResource msgRes = new MessageResource(handler, config, repo);

        Endpoint ep = new Endpoint(config, msgRes);
        String userId = ep.signIn(email, password);
        ep.connectWebSocket();

        Logger.info(String.format("Logged in as: %s, id: %s", email, userId));

        WireClient wireClient = repo.getWireClient(userId, conv);
        Conversation conversation = wireClient.getConversation();

        String sharedId = conversation.members.get(0).id;

        User user = new ArrayList<>(wireClient.getUsers(Collections.singletonList(userId))).get(0);
        User sharing = new ArrayList<>(wireClient.getUsers(Collections.singletonList(sharedId))).get(0);

        DocumentListener listener = new MyDocumentListener(wireClient);

        UI ui = new UI(String.format("%s sharing with %s", user.name, sharing.name));

        Document document = ui.getDocument();
        document.addDocumentListener(listener);

        handler.setDocument(document);
        handler.setListener(listener);

        ui.setVisible(true);
    }
}