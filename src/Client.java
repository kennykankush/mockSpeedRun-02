import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket clientSock = new Socket("localhost", 3000);
        
        InputStream is = clientSock.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        OutputStream os = clientSock.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter ow = new BufferedWriter(osw);

        Scanner scanner = new Scanner(System.in);
        String outgoing = "";
        String incoming = "";
        List<String> storage = new ArrayList<>();

        int END_COUNTER = 0;
        Boolean END_NOW = false;
        Boolean END_POPULATE = false;
        int PROD_COUNTER_END = 0;
        String id = "";
        Double budget = 0.0;
        Double initialBudget = 0.0;
        List<Item> list = new ArrayList<>();
        List<Item> sorted_List = new ArrayList<>();
        List<Item> cart = new ArrayList<>();

        while (!outgoing.equals("quit")){

            while (!END_NOW){

                incoming = br.readLine();
                System.out.println(incoming);

                if (incoming.startsWith("item_count")){
                    System.out.println("ITEM_COUNT DETECTED!");
                    String[] lineData = incoming.split(": ");
                    System.out.println(Arrays.toString(lineData));
                    END_COUNTER = Integer.parseInt(lineData[1]);
                } else if (incoming.startsWith("prod_end")){
                    System.out.println("[PROD_END DETECTED!");
                        PROD_COUNTER_END++;
                        if (PROD_COUNTER_END ==END_COUNTER){
                            System.out.println("Exiting because PROD has been exhausted");
                            END_NOW = true;
                        }
                } else {
                    System.out.println("ADDING" + incoming);
                    storage.add(incoming);
                }
        }
            
            if (END_NOW == true && END_POPULATE == false){
                System.out.println("Entering proccesing mode");
                for (int i = 0; i < storage.size(); i++){
                    if (storage.get(i).startsWith("request_id")){
                        String[] lineData = storage.get(i).split(": ");
                        id = lineData[1];

                    } else if (storage.get(i).startsWith("budget")){
                        String[] lineData = storage.get(i).split(": ");
                        budget = Double.parseDouble(lineData[1]);
                        initialBudget = Double.parseDouble(lineData[1]);
    
                    } else if (storage.get(i).equals("prod_start")){
                        String item_id = storage.get(i + 1);
                        String[] lineData1 = item_id.split("prod_id:  ");
                        item_id = lineData1[1];

                        String item_title = storage.get(i + 2);
                        String[] lineData2 = item_title.split("title:  ");
                        item_title = lineData2[1];
                        
                        String item_price = storage.get(i+3);
                        String[] lineData3 = item_price.split("price:  ");
                        Double Ditem_price = Double.parseDouble(lineData3[1]);

                        String item_rating = storage.get(i+4);
                        String[] lineData4 = item_rating.split("rating:  ");
                        Double Ditem_rating = Double.parseDouble(lineData4[1]);

                        Item new_Item = new Item(item_id,item_title,Ditem_price,Ditem_rating);
                        list.add(new_Item);

                    } else if (i == storage.size() - 1){
                        END_POPULATE = true;
                        System.out.println("items have been processed to List<Item> list");

                    }
                        
                }

            }

            System.out.println("Entering input mode");

            outgoing = scanner.nextLine();

            if (outgoing.equals("check")){
                    System.out.println(list);
                    System.out.println(list.get(1));
                    System.out.println(id);
                    System.out.println(budget);
                    System.out.println(list.get(2) + "\n");
                    System.out.println(list.get(1)+ "\n");
    
            } else if (outgoing.equals("check storage")){
                System.out.println(storage);

            } else if (outgoing.equals("do it")){  //SORT THE LIST INTO ANOTHER LIST CALLED SORTED_LIST
                sorted_List = list.stream()
                                        .sorted(Comparator.comparing(Item::getRating).reversed()
                                        .thenComparing(Comparator.comparing(Item::getPrice).reversed()))
                                        .collect(Collectors.toList());

                sorted_List.forEach(item -> System.out.println(item));
                // sorted_List.forEach(System.out::println);
                
            } else if (outgoing.equals("budget")){  //BUDGET ALGO
                System.out.println("Budget is : " + budget);
                for (Item x: sorted_List){
                    if (x.getPrice() > budget){
                        continue;
                    } else {
                        cart.add(x);
                        budget -= x.getPrice();
                    }
                }

                cart.forEach(item -> System.out.println(item));
            } else if (outgoing.equals("please")){   //GET RESULTS
                String idString = cart.stream()
                                 .map(item -> String.valueOf(item.getId()))
                                .collect(Collectors.joining(", "));    //TURNS ALL the ID into for e.g [14, 5, 7, 2, 9, 5]

                Double spent = initialBudget - budget;   

                StringBuilder sb = new StringBuilder();
                sb.append("request_id: " + id).append("\n");
                sb.append("name: ronaldo\n");
                sb.append("email: ronaldo@gmail.com\n");
                sb.append("items: " + idString).append("\n");
                sb.append("spent: " + spent).append("\n");
                sb.append("remaining: " + budget).append("\n");
                sb.append("client_end").append("\n");

                String sent_out = sb.toString();
                ow.write(sent_out);
                ow.flush();
                System.out.println("Sending " +sent_out);

                incoming = br.readLine();
                System.out.println(incoming);   //receives success or nah
                
            }

            System.out.println("end");
            
    
            }
            
            // incoming = dis.readUTF();
            // System.out.println(incoming);
        
        scanner.close();
        ow.close();
        osw.close();
        os.close();
        br.close();
        isr.close();
        is.close();
        clientSock.close();

    } 
    
}
