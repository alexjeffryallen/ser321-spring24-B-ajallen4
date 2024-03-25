import org.junit.Test;
import static org.junit.Assert.*;
import org.json.JSONObject;

public class Testing {

    // some tests for locally testing methods in the server
    @Test
    public void typeWrong() {
        JSONObject req = new JSONObject();
        req.put("type1", "echo");

        JSONObject res = SockServer.testField(req, "type");

        assertEquals(res.getBoolean("ok"), false);
        assertEquals(res.getString("message"), "Field type does not exist in request");
    }

    @Test
    public void echoCorrect() {
        JSONObject req = new JSONObject();
        req.put("type", "echo");
        req.put("data", "whooooo");
        JSONObject res = SockServer.echo(req);

        assertEquals("echo", res.getString("type"));
        assertEquals(res.getBoolean("ok"), true);
        assertEquals(res.getString("echo"), "Here is your echo: whooooo");
    }

    @Test
    public void echoErrors() {
        JSONObject req = new JSONObject();
        req.put("type", "echo");
        req.put("data1", "whooooo");
        JSONObject res = SockServer.echo(req);

        assertEquals(res.getBoolean("ok"), false);
        assertEquals(res.getString("message"), "Field data does not exist in request");

        JSONObject req2 = new JSONObject();
        req2.put("type", "echo");
        req2.put("data", 33);
        JSONObject res2 = SockServer.echo(req2);

        assertEquals(false, res2.getBoolean("ok"));
        assertEquals(res2.getString("message"), "Field data needs to be of type: String");

        JSONObject req3 = new JSONObject();
        req3.put("type", "echo");
        req3.put("data", true);
        JSONObject res3 = SockServer.echo(req3);

        assertEquals(res3.getBoolean("ok"), false);
        assertEquals(res3.getString("message"), "Field data needs to be of type: String");
    }
}