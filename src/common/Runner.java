package common;

public class Runner {
    public static void main(String[] args) throws Exception {


        Client client = new Client("MTL");

       // test create Trecord
        client.createTRecord("Maxim", "Beautin", "Blvd Saint-Andre", "514-333-4493", "Finance", "MTL");

    }
}
