package pgdp.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;
import pgdp.networking.DataHandler.ConnectionException;
import pgdp.networking.ViewController.Message;
import pgdp.networking.ViewController.User;

public class DataHandler {

    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;

    private Queue<Byte> handshakeMutex;
    private Thread inputHandler;

    private HttpClient client;
    private int id;
    private String username;
    private String password;
    
    public static String serverAddress = "carol.sse.cit.tum.de";
    
    private final static byte SUPPORTED_VERSION = 42;

    boolean connected;

    /**
     * Erstellt neuen HTTP Client für die Verbindung zum Server
     */
    public DataHandler() {
        handshakeMutex = new LinkedList<>();

        /************************
         * Your Code goes here: *
         ************************/
        client = HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

/************************************************************************************************************************
 *                                                                                                                       *
 *                                       HTTP Handling                                                                   *
 *                                                                                                                       *
 *************************************************************************************************************************/

    /**
     * Registriert den Nutzer beim Server oder erfragt ein neues Passwort
     * Gibt bei Erfolg true zurück.
     * Endpoint: /api/user/register
     * @param username Nutzername
     * @param kennung TUM Kennung
     * @return Registrierung erfolgreich
     */
    public boolean register(String username, String kennung) {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://" + serverAddress + "/api/user/register"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString("{\"username\": \"" + username + "\",\"tum_kennung\": \"" + kennung + "\"}"))
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return false;
        }

        return response.statusCode() == 200;
    }

    /**
     * Hilfsmethode um nach erfolgreichem Login einen Authentifizierungstoken zu erhalten.
     * Returns null upon failure
     * @return Authentication token or null
     */
    public String requestToken() {

        if (this.username == null || this.password == null) {
            return null;
        }

        return requestToken(this.username, this.password);
    }

    /**
     * Erfragt Autentifizierungstoken vom Server.
     * Gibt null bei Fehler zurück
     * Endpoint: /token
     * @param username Nutzername
     * @param password Passwort
     * @return token oder null
     */
    private String requestToken(String username, String password) {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://" + serverAddress + "/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString("username=" + username + "&password=" + password))
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return null;
        }

        if (response.statusCode() == 200) {     // Prüft, ob die Anfrage erfolgreich war
            JSONObject jsonObject = new JSONObject(response.body());    // Erstellt ein JSONObject aus dem body der Antwort
            return jsonObject.getString("access_token");    // Liest den Token aus und gibt ihn zurück
        }

        return null;
    }

    /**
     * Initialer login.
     * Wenn ein Token mit Nutzername und Passwort erhalten wird, werden diese gespeichert.
     * Anschließend wird die Nutzer ID geladen.
     * Endpoint: /token
     *           /api/user/me
     * @param username Nutzername
     * @param password Passwort
     * @return Login erfolgreich
     */
    public boolean login(String username, String password) {
        String token = requestToken(username, password);    // token wird angefragt und gespeichert.
        if (token == null) return false;    // wenn die token-Anfrage nicht erfolgreich war, wird schon jetzt false zurückgegeben
        this.username = username;   // Attribute des DataHandlers werden gesetzt.
        this.password = password;

        HttpRequest request = HttpRequest.newBuilder(URI.create("http://" + serverAddress + "/api/user/me/"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return false;
        }

        if (response.statusCode() == 200) {
            this.id = new JSONObject(response.body()).getInt("id");
            return true;
        }

        return false;
    }

    /**
     * Erfragt alle öffentlichen Nutzer vom Server
     * Endpoint: /api/users
     * @return Map von Nutzern und IDs
     */
    public Map<Integer, User> getContacts() {


        HttpRequest request = HttpRequest.newBuilder(URI.create("http://" + serverAddress + "/api/users"))
                .header("Authorization", "Bearer " + requestToken(username, password))
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return null;
        }

        if (response.statusCode() != 200) return null;

        Map<Integer, User> result = new HashMap<>();
        JSONArray jsonArray = new JSONArray(response.body());

        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject jsonObject) {
                result.put(
                        jsonObject.getInt("id"),
                        new User(
                                jsonObject.getInt("id"),
                                jsonObject.getString("username"),
                                new ArrayList<>()));
            }
        }

        return result;
    }

    /**
     * Erfragt alle Nachrichten, welche mit einem gewissen Nutzer ausgetauscht wurden.
     * Endpoint: /api/messages/with/
     * @param id ID des Partners
     * @param count Anzahl der zu ladenden Nachrichten
     * @param page Falls count gesetzt, gibt die Seite an Nachrichten an.
     * @return Liste der Abgefragten Nachrichten.
     */
    public List<Message> getMessagesWithUser(int id, int count, int page) {
        HttpRequest request = HttpRequest
                .newBuilder(URI.create("http://" + serverAddress + "/api/messages/with/"
                        + Long.toString(id)
                        + "?limit=" + Integer.toString(count)
                        + "&pagination=" + Integer.toString(page)))
                .header("Authorization", "Bearer " + requestToken(username, password))
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return null;
        }

        if (response.statusCode() != 200) return null;  // Wenn Anfrage nicht erfolgreich war, wird abgebrochen

        List<Message> result = new ArrayList<>();   // Liste zum Zurückgeben wird erstellt
        JSONArray jsonArray = new JSONArray(response.body());  // JSONArray der Serverantwort wird erstellt

        for (Object obj : jsonArray) {  // Es wird durch alle Messages durch-iteriert
            if (obj instanceof JSONObject jsonObject) {
                result.add(new Message(     // Nachrichten werden in die List hinzugefügt
                        LocalDateTime.parse(jsonObject.getString("time")),  // Timestamp
                        jsonObject.getString("text"),   // Inhalt der Nachricht
                        (jsonObject.getInt("from_id") == this.id),  // true: von mir geschrieben. false: vom anderen geschrieben.
                        id  // id des anderen
                ));
            }
        }

        List<Message> messagePage = new ArrayList<>();  // Nur die Nachrichten auf der angeforderten Seite
        for (int i = page * count; i < result.size() && i < (page+1) * count; i++)  // Es wird auf der Seite, die Angefordert wird angefangen.
            messagePage.add(result.get(i)); // Alle Nachrichten werden kopiert

        return messagePage;
    }

    /*-**********************************************************************************************************************
    *                                                                                                                       *
    *                                       Socket Handling                                                                 *
    *                                                                                                                       *
    *************************************************************************************************************************/

    /**
     * Thread Methode um ankommende Nachrichten zu behandeln
     */
    private void handleInput() {

        System.out.println("Input Handler started");

        try {
            while (true) {

                byte type = in.readByte();
                System.out.println("Recieved Message");

                switch (type) {
                    case 0 -> {
                        byte hsType = in.readByte();
                        if (hsType == 5) {

                            passHandshakeMessage(new byte[] {type, hsType});
                        }
                    }
                    case 1 -> {

                        int length = ((in.readByte()&0xff)<<8) | (in.readByte()&0xff);

                        byte[] content = new byte[length];
                        in.read(content);

                        displayMessage(new String(content, StandardCharsets.UTF_8));
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }
    }

    private byte[] intToByteArray(int val) {
        return new byte[] { (byte) (val >> 24), (byte) (val >> 16), (byte) (val >> 8), (byte) val };
    }

    /**
     * Erstelle einen Socket und Verbinde mit dem Server.
     * Gebe Nutzer ID und Token an. Verifiziert Server Antworten
     * @throws ConnectionException
     */
    private void connect() throws ConnectionException {
        try {
            Socket sock = new Socket(serverAddress, 1337);
            DataInputStream din = new DataInputStream(sock.getInputStream());
            DataOutputStream dout = new DataOutputStream(sock.getOutputStream());

            byte[] reader = new byte[3];
            din.read(reader);   // Server hello
            if (reader[0] != 0x00 || reader[1] != 0x00 || reader[2] != SUPPORTED_VERSION)
                throw new ConnectionException("Server Handshake failure");

            dout.write(new byte[] {0x00, 0x01}); // Client Hello

            byte[] idBytes = intToByteArray(id);
            byte[] idMessage = new byte[] {0x00, 0x02, 0x04, 0, 0, 0, 0};
            for (int i = 3; i < idMessage.length; i++) idMessage[i] = idBytes[i-3];
            dout.write(idMessage); // Client Identification

            String token = requestToken(username, password);
            if (token == null) throw new ConnectionException("Request Token Error");
            byte[] tokenLenght = new byte[] {(byte) (token.length() >> 8), (byte) token.length() };
            byte[] tokenBytes = token.getBytes();
            byte[] tokenMessage = new byte[token.length() +4];
            tokenMessage[0] = 0x00;
            tokenMessage[1] = 0x03;
            System.arraycopy(tokenLenght, 0, tokenMessage, 2, 2);
            System.arraycopy(tokenBytes, 0, tokenMessage, 4, tokenBytes.length);
            dout.write(tokenMessage); // Client Authentication
            dout.flush();

            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());
            socket = sock;

            startInputHandler();
            connected = true;
        } catch (Throwable t) {
            if (t.getClass().equals(ConnectionException.class)) {
                throw (ConnectionException) t;
            }

            t.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Wechselt die Verbindung zu einem anderen Chatpartner
     * @param partnerID
     * @throws ConnectionException
     */
    public void switchConnection(int partnerID) throws ConnectionException{
        try {
            if (!connected) {
                connect();
            }
            // Client to Server
            byte[] switchMessage = new byte[] {0x00, 0x04, 0x04, 0, 0, 0, 0};
            byte[] switchBytes = ByteBuffer.allocate(4).putInt(partnerID).array();
            System.arraycopy(switchBytes, 0, switchMessage, 3, 4);
            out.write(switchMessage);
            out.flush();

            // Server to Client
            byte[] serverMessage = getResponse(2);
            if (serverMessage[0] != 0x00 || serverMessage[1] != 0x05) throw new ConnectionException("Switch Partner Server response wrong");


        } catch (Throwable t) {

            if (t.getClass().equals(ConnectionException.class)) {
                throw (ConnectionException)t;
            }
            t.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Sende eine Nachricht an den momentan ausgewählten Nutzer.
     * @param message
     */
    public void sendMessage(String message) {
        try {
            // Kodiert die übergebene 'message' in UTF-8
            byte[] buf = StandardCharsets.UTF_8.encode(message).array();
            int length = Math.min(buf.length, 0xffff);

            byte[] messageLenght = new byte[] {(byte) (length >> 8), (byte) length};
            byte[] Message = new byte[length +3];
            Message[0] = 0x01;
            for (int i = 1; i < 3; i++) Message[i] = messageLenght[i-1];
            for (int i = 3; i < Message.length; i++) Message[i] = buf[i-3];
            out.write(Message);

        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }

    }


    /**
     * Holt sich length bytes vom empfänger Thread
     * @param length anzahl an bytes
     * @return
     */
    private byte[] getResponse(int length) {

        boolean wait = true;
        byte[] resp = new byte[length];

        synchronized(handshakeMutex) {
            wait = handshakeMutex.size() < length;
        }

        while (wait) {
            synchronized(inputHandler) {
                try {
                    inputHandler.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            synchronized(handshakeMutex) {
                wait = handshakeMutex.size() < length;
            }
        }

        synchronized(handshakeMutex) {
            for (int i = 0; i < resp.length; i++) {
                resp[i] = handshakeMutex.remove();
            }
        }
        return resp;
    }

    /**
     * Startet einen neuen thread für das input handling.
     */
    private void startInputHandler() {

        inputHandler = new Thread() {
            @Override
            public void run() {
                handleInput();
            }
        };
        inputHandler.start();

    }

    /**
     * Übergibt eine Nachricht an die Nutzeroberfläche
     * @param content Nachrichten inhalt
     */
    private void displayMessage(String content) {
        Platform.runLater(() -> {
            ViewController.displayMessage(ViewController.currentChat, new Message(LocalDateTime.now(), content, false, 0));
        });
    }

    /**
     * Übergibt eine Handshake Nachricht an den Hauptthread
     * @param handshake Nachricht
     */
    private void passHandshakeMessage(byte[] handshake) {
        synchronized(handshakeMutex) {

            for (byte b : handshake) {
                handshakeMutex.add(b);
            }
        }

        synchronized(inputHandler) {
            inputHandler.notifyAll();
        }
        System.out.println("Notified main thread");
    }

    /**
     * Setter fürs testing
     * @param client
     */
    public void setClient(HttpClient client) {
        this.client = client;
    }
    
    /**
     * Schlißet offene Verbindungen
     */
    public void close() {
        if (inputHandler != null) {
            inputHandler.interrupt();
        }
        if (socket != null) {
            try {
                out.write(new byte[] {0,-1});
                socket.close();
            } catch (IOException e) {
                // pass
            }
        }
    }

    public static class ConnectionException extends Exception {

        private static final long serialVersionUID = 9055969838018372992L;

        public ConnectionException() {super();}
        public ConnectionException(String message) {super(message);}

    }
}