import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import Entities.*;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import com.db4o.query.Predicate;

/**
 * @author Joan Anton Perez Branya
 * @since 19/02/2017
 *
 */

public class TShirtsDB4O {
	public static ArrayList<Order> orders;
	static ObjectContainer db;
	

	/**
	 * Implement TODO methods and run to test
	 * 
	 * @param args
	 *            no args
	 * @throws IOException
	 *             in order to read files
	 * @throws ParseException
	 *             in order to parse data formats
	 */
	public static void main(String[] args) throws IOException, ParseException {
		TShirtsDB4O TSM = new TShirtsDB4O();
		FileAccessor fa = new FileAccessor();
		fa.readArticlesFile("articles.csv");
		fa.readCreditCardsFile("creditCards.csv");
		fa.readCustomersFile("customers.csv");
		fa.readOrdersFile("orders.csv");
		fa.readOrderDetailsFile("orderDetails.csv");

		orders = fa.orders;
		try {

			File file = new File("orders.db");
			String fullPath = file.getAbsolutePath();
			db = Db4o.openFile(fullPath);

			TSM.addOrders();
			TSM.listOrders();
			TSM.listArticles();
			TSM.addArticle(7, "CALCETINES EJECUTIVOS 'JACKSON 3PK'", "gris", "45", 18.00f);
			TSM.updatePriceArticle(7, 500.59f); //12.00f
			TSM.llistaArticlesByName("CALCETINES EJECUTIVOS 'JACKSON 3PK'");
			TSM.deletingArticlesByName("POLO BÁSICO 'MANIA'");
			TSM.deleteArticleById(7);
			TSM.listArticles();
			TSM.listCustomers();
			TSM.changeCreditCardToCustomer(1);
			TSM.listCustomers();
			TSM.llistaCustomerByName("Laura");
			TSM.showOrdersByCustomerName("Laura");
			TSM.showCreditCardByCustomerName("Laura");
			TSM.deleteCustomerbyId(2);
			TSM.retrieveOrderContentById_Order(2);
			TSM.deleteOrderContentById_Order(2);
			TSM.retrieveOrderContentById_Order(2);
			TSM.listCustomers();
			TSM.clearDatabase();
			TSM.listOrders();

		} finally {
			// close database
			db.close();
		}
	}

	/**
	 * Select Customer using customer id and next generate a new credit card and
	 * update customer using the new credit card
	 * 
	 * @param i
	 *            idCustomer
	 */
	public void changeCreditCardToCustomer(int i) {
		// TODO Auto-generated method stub
		ObjectSet<Entities.Customer> result = db.queryByExample(new Entities.Customer(i, null, null, null, null,null));
		Customer customer=(Customer) result.next();
		CreditCard creditCard = new CreditCard("1818000000Z","1144",3,2022);
		customer.setCreditCard(creditCard);
	}

	/**
	 * Select Article using id and next update price
	 * 
	 * @param id
	 *            article
	 * @param newPrice
	 */
	public void updatePriceArticle(int id, float newPrice) {
		// TODO Auto-generated method stub
		ObjectSet<Entities.Article> result = db.queryByExample(new Entities.Article(id, null, null, null, 00.00f));
		Article article=(Article) result.next();
		article.setRecommendedPrice(newPrice);
	}

	/**
	 * Add a new article into database
	 * 
	 * @param i
	 *            article id
	 * @param string
	 *            article name
	 * @param string2
	 *            article colour
	 * @param string3
	 *            article size
	 * @param d
	 *            article price
	 */
	public void addArticle(int i, String string, String string2, String string3, float d) {
		// TODO Auto-generated method stub
		Article article = new Article(i,string,string2,string3,d);
		db.store(article);
	}

	/**
	 * Delete an article using idArticle
	 * 
	 * @param i
	 *            idArticle
	 */
	public void deleteArticleById(int i) {
		// TODO Auto-generated method stub
		ObjectSet<Entities.Article> result = db.queryByExample(new Entities.Article(i, null, null, null, 00.00f));
		while(result.hasNext()) {
			db.delete(result.next());
		}
	}

	/**
	 * Delete Order and its orderdetails using idOrder
	 * 
	 * @param i
	 *            idOrder
	 */
	public void deleteOrderContentById_Order(int i) {
		// TODO Auto-generated method stub
		ObjectSet<Entities.Order> result = db.queryByExample(new Entities.Order(i, null, null, null,null));
		while(result.hasNext()) {
			db.delete(result.next());
		}
	}

	/**
	 * Select Order using his id and order details
	 * 
	 * @param i
	 *            idOrder
	 */
	public void retrieveOrderContentById_Order(int i) {
		// TODO Auto-generated method stub
		System.out.println("__________________________");
		System.out.println("RETRIEVE ORDER BY ID ORDER");
		System.out.println("--------------------------");
		List<Order> orders = db.query(new Predicate<Order>() {
			@Override
			public boolean match(Order order) {
				if (order.getIdOrder() == i)
					return true;
				else return false;
			}
		});
		for (Order order : orders) {
			System.out.println(order.toString());
		}
	}

	/**
	 * Delete Customer using idCustomer
	 * 
	 * @param i
	 *            idCustomer
	 */
	public void deleteCustomerbyId(int i) {
		// TODO Auto-generated method stub
		ObjectSet<Entities.Customer> result = db.queryByExample(new Entities.Customer(i, null, null, null, null,null));
		while(result.hasNext()) {
			db.delete(result.next());
		}
	}

	/**
	 * Select Customer using customer name and next select the credit card
	 * values
	 * 
	 * @param string
	 *            customer name
	 */
	public void showCreditCardByCustomerName(String string) {
		// TODO Auto-generated method stub
		System.out.println("_________________________________");
		System.out.println("SHOW CREDIT CARD BY CUSTOMER NAME");
		System.out.println("---------------------------------");
		List<Customer> customers = db.query(new Predicate<Customer>() {

			@Override
			public boolean match(Customer customer) {
				if (customer.getName().compareTo(string) == 0)
					return true;
				else return false;
			}
		});
		for (Customer customer : customers) {
			System.out.println(customer.getCreditCard().toString());
		}
	}

	/**
	 * Method to list Oders and orderdetails from the database using the
	 * customer name
	 */
	public void showOrdersByCustomerName(String string) {
		// TODO Auto-generated method stub
		System.out.println("____________________________");
		System.out.println("SHOW ORDERS BY CUSTOMER NAME");
		System.out.println("----------------------------");
		List<Order> orders = db.query(new Predicate<Order>() {

			@Override
			public boolean match(Order order) {
				if (order.getCustomer().getName().compareTo(string) == 0)
					return true;
				else return false;
			}
		});
		for (Order order : orders) {
			System.out.println(order.toString());
		}
	}

	/** delete all objects from the whole database */
	public void clearDatabase() {
		// TODO Auto-generated method stub
		List<Article> articles = db.query(new Predicate<Article>() {
			@Override
			public boolean match(Article article) {
				return true;
			}
		});
		for (Article article:articles) {
			db.delete(article);
		}

		List<Order> orders = db.query(new Predicate<Order>() {
			@Override
			public boolean match(Order order) {
				return true;
			}
		});
		for (Order order:orders) {
			db.delete(order);
		}

		List<OrderDetail> orderDetails = db.query(new Predicate<OrderDetail>() {
			@Override
			public boolean match(OrderDetail orderDetail) {
				return true;
			}
		});
		for (OrderDetail orderDetail:orderDetails) {
			db.delete(orderDetail);
		}

		List<Customer> customers = db.query(new Predicate<Customer>() {
			@Override
			public boolean match(Customer customer) {
				return true;
			}
		});
		for (Customer customer:customers) {
			db.delete(customer);
		}

		List<CreditCard> creditCards = db.query(new Predicate<CreditCard>() {
			@Override
			public boolean match(CreditCard creditCard) {
				return true;
			}
		});
		for (CreditCard creditCard:creditCards) {
			db.delete(creditCard);
		}
	}

	/**
	 * Delete Article using article name
	 * 
	 * @param string
	 *            Article name
	 */
	public void deletingArticlesByName(String string) {
		// TODO Auto-generated method stub
		ObjectSet<Entities.Article> result = db.queryByExample(new Entities.Article(0, string, null, null, 00.00f));
		while(result.hasNext()) {
			db.delete(result.next());
		}
	}

	/** Method to list Articles from the database using their name */
	public void llistaArticlesByName(String string) {
		// TODO Auto-generated method stub
		System.out.println("____________________");
		System.out.println("LIST ARTICLE BY NAME");
		System.out.println("--------------------");
		List<Article> articles = db.query(new Predicate<Article>() {
			@Override
			public boolean match(Article article) {
				if (article.getName().compareTo(string) == 0)
					return true;
				else return false;
			}
		});
		for (Article article:articles) {
			System.out.println(article.toString());
		}
	}

	/** Method to list Customers from the database using their name */
	public void llistaCustomerByName(String string) {
		// TODO Auto-generated method stub
		System.out.println("____________");
		System.out.println("LIST BY NAME");
		System.out.println("------------");
		List<Customer> customers = db.query(new Predicate<Customer>() {
			@Override
			public boolean match(Customer customer) {
				if (customer.getName().compareTo(string) == 0)
				return true;
				else return false;
			}
		});
		for (Customer customer:customers) {
			System.out.println(customer.toString());
		}
	}

	/** Method to list all Customers from the database */
	public void listCustomers() {
		// TODO Auto-generated method stub
		ObjectSet<Entities.Customer> result = db.queryByExample(new Entities.Customer());
		System.out.println(result.size());
		while (result.hasNext()) {
			System.out.println(result.next());
		}
	}

	/** Method to list all Articles from the database */
	public void listArticles() {
		// TODO Auto-generated method stub
		ObjectSet<Entities.Article> result = db.queryByExample(new Entities.Article());
		System.out.println(result.size());
		while (result.hasNext()) {
			System.out.println(result.next());
		}
	}

	/** Method to add all orders from ArrayList and store them into database */
	public void addOrders() {
		// TODO Auto-generated method stub
		for (int i = 0; i < orders.size(); i++) {
			db.store(orders.get(i));
		}
	}

	/** Method to list all Orders from the database */
	public void listOrders() {
		// TODO Auto-generated method stub
		ObjectSet<Entities.Order> result = db.queryByExample(new Entities.Order());
		System.out.println(result.size());
		while (result.hasNext()) {
			System.out.println(result.next());
		}
	}
}
