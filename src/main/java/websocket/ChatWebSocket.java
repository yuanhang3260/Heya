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
import dao.UserDAO;
import websocket.HttpSessionConfigurator;

@ServerEndpoint(value = "/chat/{username}", configurator = HttpSessionConfigurator.class)
public class ChatWebSocket {
  private static final Logger log = Logger.getLogger(ChatWebSocket.class);

  private UserDAO userDAO;
  private HttpSession httpSession;

  private UserDAO getUserDAO() {
    if (this.userDAO == null) {
      this.userDAO = (UserDAO)SpringUtils.getBean("UserDAO");
    }
    return this.userDAO;
  }

  @OnOpen
  public void onOpen(Session session,
                     EndpointConfig config,
                     @PathParam("username")String username) {
    log.info("Connected ... " + session.getId());
    log.info("getQueryString " + session.getQueryString());
    log.info("getRequestURI " + session.getRequestURI());

    this.httpSession = (HttpSession)(config.getUserProperties().get(HttpSession.class.getName()));
    User user = (User)(this.httpSession.getAttribute("user"));
    log.info(user.getUsername());
    log.info(user.getUid());
  }

  @OnMessage
  public String onMessage(String message, Session session) {
    log.info("Received: " + message);
    return message;
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    log.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
  }
}
