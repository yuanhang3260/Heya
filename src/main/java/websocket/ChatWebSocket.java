package websocket;
 
import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.apache.log4j.Logger;

import application.SpringUtils;
import bean.User;
import controller.ChatDispatcher;
import websocket.HttpSessionConfigurator;

@ServerEndpoint(value = "/chat/{username}", configurator = HttpSessionConfigurator.class)
public class ChatWebSocket {
  private static final Logger log = Logger.getLogger(ChatWebSocket.class);

  private ChatDispatcher dispatcher;
  private HttpSession httpSession;
  private String username;

  private ChatDispatcher getDispatcher() {
    if (this.dispatcher == null) {
      this.dispatcher = (ChatDispatcher)SpringUtils.getBean("ChatDispatcher");
    }
    return this.dispatcher;
  }

  @OnOpen
  public void onOpen(Session session,
                     EndpointConfig config,
                     @PathParam("username")String username) {
    log.info("Connected ... " + session.getId());

    this.httpSession = (HttpSession)(config.getUserProperties().get(HttpSession.class.getName()));
    User user = (User)(this.httpSession.getAttribute("user"));
    this.username = user.getUsername();
    if (!username.equals(this.username)) {
      log.info("Unauthorized user " + username);
      return;
    }

    getDispatcher().newSessionOpen(username, session);
  }

  @OnMessage
  public String onMessage(String message, Session session) {
    log.info("Received: " + message);
    getDispatcher().processMessage(this.username, message, session);
    return message;
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    log.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    getDispatcher().sessionClosed(this.username, session);
  }
}
