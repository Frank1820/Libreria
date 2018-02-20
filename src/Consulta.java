

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;





@WebServlet("/consulta")
public class Consulta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	DataSource pool;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		try {
			InitialContext contexto= new InitialContext();
			
			 pool=(DataSource)contexto.lookup("java:comp/env/jdbc/mysql_tiendalibros");
			
			if (pool==null) {
				throw new ServletException("ERROR AL ACCEDER AL POOL DE CONEXIONES: POOL");
				
			}
			
			
		} catch (NamingException e) {
			System.out.println("ERROR AL ACCEDER AL POOL DE CONEXIONES");
			e.printStackTrace();
		}
		
	}
       

    public Consulta() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Recogemos el parametro "autor" del formulario.
		String autor= request.getParameter("autor");
		
		//Premaramos la salida del servlet
		HttpSession sesion= request.getSession();
		sesion.invalidate();
		
		response.setContentType("text/html");
		PrintWriter out =response.getWriter();
		
		//Objetos para la conexion y consulta a la base de datos
		Connection con=null;
		PreparedStatement stmt=null;
		
		//Cargar driver de MySQL
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			//Credenciales para la base de datos
//			String url="jdbc:mysql://localhost/TiendaLibros";
//			
//			String usuario="librero";
//			String password="Ageofempires2";
			
			//Ejecutamos una consulta SQL.
			con= pool.getConnection();
			
			//con=DriverManager.getConnection(url, usuario, password);
			
			String sql="Select * from libros where autor =?";
			stmt=con.prepareStatement(sql);
			
			
			stmt.setString(1, autor);
			
			ResultSet resultados = stmt.executeQuery();
			
			//Mostramos los resultados
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Consulta</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Consulta de libros por autor</h1>");
			out.println("<p>Libros escritos por " + autor + "</p>");
			
			while (resultados.next()) {
				out.println("Título: "+resultados.getString("titulo") +"<br>");
				out.println("Precio: "+ resultados.getString("precio") +"<br>");
				out.println("Cantidad: "+resultados.getString("cantidad") +"<br>");
				out.println("<hr>");
			}
			
			out.println("</body>");
			out.println("</html>");
			
			
			
			
			
			
			
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("ERROR EN LA CONEXIÓN A LA BASE DE DATOS");
			e.printStackTrace();
		}finally{
			try {
				con.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("ERROR AL CERRAR LA BASE DE DATOS");
				e.printStackTrace();
			}
		}
		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
