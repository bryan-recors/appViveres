package com.example.appviveresprimavera;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 17/07/2021
@fModificación 17/07/2021
@descripción: Base de Datos
*/

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class BaseDatos extends SQLiteOpenHelper {
    private static final String nombreBdd = "bdd_viveres"; //definiendo el nombre dela Bdd
    private static final int versionBdd = 7; //definiendo la version de la BDD

    //estructura de la tabla productos

    private static final String tablaProducto= "create table producto(id_prod integer primary key autoincrement," +
            "nombre_prod text, fecha_prod text, stock_prod integer, precio_prod double, URLimagen_prod text, descripcion_prod text);"; // definiendo estructura de la tabla usuarios

    private static final String tablaUsuario="create table usuario(id_usu integer primary key autoincrement, nombre_usu text, direccion_usu text," +
            "email_usu text,password_usu text,tipo_usu text);";//definir la estructura de la tabla de usuarios

    //esta se esta usando como carrito
    private static final String tablaDetalleVenta = "create table detalle_venta(id_det integer primary key autoincrement," +
            "producto_det text, precio_det double, cantidad_det integer, subtotal_det double, fk_id_usu integer, foreign key(fk_id_usu) references usuario(id_usu))";

    private static final String tablaVenta = "create table venta(id_ven integer primary key autoincrement," +
            "fecha_ven text,total_ven double,estado_ven text, fk_id_usu integer, foreign key (fk_id_usu) references usuario (id_usu))";

    //esta se esta usando como detalle del pedido y esta relacinada con la venta para que esta pueda ser despachada
    private static final String tablaDetallePedidoVenta = "create table detalle_pedido_venta(id_det_ped integer primary key autoincrement," +
            "producto_det_ped text, precio_det_ped double, cantidad_det_ped integer, subtotal_det_ped double, fk_id_venta integer, foreign key(fk_id_venta) references venta(id_ven))";

    /*
    private static final String tablaProducto= "create table producto(id_prod integer primary key autoincrement," +
            "nombre_prod text, fecha_prod text, stock_prod integer, precio_prod double, imagen_prod BLOB, URLimagen_prod text, descripcion_prod text)"; // definiendo estructura de la tabla usuarios
     */

    //Constructor
    public BaseDatos(Context contexto) {
        super(contexto, nombreBdd, null, versionBdd);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ejecutando el query DDl(sentencia de definicion de datos) para crear la tabla
        db.execSQL(tablaProducto);
        db.execSQL(tablaUsuario);
        db.execSQL(tablaDetalleVenta); //carrito
        db.execSQL(tablaVenta);
        db.execSQL(tablaDetallePedidoVenta);//detalle de la venta o pedido

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS producto");//elimincacion de la version anterior de la tabla usuarios se puee usar otro comando Dll como alter table
        db.execSQL(tablaProducto); //Ejecucion del codigo para crear la tabla usuaios con su nueva estructura
        db.execSQL("DROP TABLE IF EXISTS usuario");// elimina de la version anterior de la tabla usuario
        db.execSQL(tablaUsuario);
        //carrito
        db.execSQL("DROP TABLE IF EXISTS detalle_venta");// elimina de la version anterior de la tabla usuario
        db.execSQL(tablaDetalleVenta);
        db.execSQL("DROP TABLE IF EXISTS venta");// elimina de la version anterior de la tabla usuario
        db.execSQL(tablaVenta);
        //detalle de la venta
        db.execSQL("DROP TABLE IF EXISTS detalle_pedido_venta");// elimina de la version anterior de la tabla usuario
        db.execSQL(tablaDetallePedidoVenta);
    }

    //agregar producto (String nombre, String fecha, int stock, double precio, byte[] imagen,  String URLimagen, String descripcion)
    public boolean agregarProducto(String nombre, String fecha, int stock, double precio, String URLimagen, String descripcion){
        SQLiteDatabase miBdd=getWritableDatabase(); //instanciamos la BDD y llamamos a la BDD en el objeto miBdd
        if(miBdd != null){ //validar la BDD
            //realiza el proceso de insercion
            miBdd.execSQL("insert into producto(nombre_prod, fecha_prod, stock_prod, precio_prod, URLimagen_prod, descripcion_prod) " +
                    "values  ('"+nombre+"','"+fecha+"','"+stock+"','"+precio+"','"+URLimagen+"','"+descripcion+"')");

            miBdd.close(); //cerrando la conexion a la bdd
            return true; //retorno cuando la insercion es exitosa
        }
        return false; //retorno cuando no existe la bdd
    }

    //PROCESO 6: metodo para consultar clientes existentes en la BDD
    public Cursor obtenerProductos(){

        SQLiteDatabase miBdd = getWritableDatabase(); //llamado a la bdd
        //consultando los productos en la BDD y guardandolos en un cursor
        Cursor productos=miBdd.rawQuery("select * from producto", null);
        if(productos.moveToFirst()){ //verifica que el objeto tenga resultados
            miBdd.close(); //cerrando la conexion a la bdd
            return productos; //retorna el cursos que contiene el listado de clientes
        }else{
            return null; //retorna nulo cuando no hay productos dentro de la tabla
        }
    }

    public boolean actualizarProducto(String nombre, String fecha, int stock, double precio, String imagen,
                                      String descripcion, String id){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd
        if (miBdd != null){ //validando que la bdd realmente exista
            //proceso de actualizacion
            miBdd.execSQL("update producto set nombre_prod='"+nombre+"', " +
                    "fecha_prod='"+fecha+"', stock_prod='"+stock+"', precio_prod='"+precio+"', " +
                    "URLimagen_prod='"+imagen+"',descripcion_prod='"+descripcion+"' where id_prod="+id);
            miBdd.close(); //cerrando la conexion a la bdd
            return true; //retornando verdadero ya que el procesos de actualizacion fue exitoso
        }
        return false; //se retorna falso cuando no existe la bdd
    }

    public boolean eliminarProducto(String id){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd

        if (miBdd != null){ //validando que la bdd realmente exista

            //proceso de eliminacion
            miBdd.execSQL("delete from producto where id_prod="+id);
            miBdd.close(); //cerrando la conexion a la bdd
            return true; //retornando verdadero ya que el proceso de eliminacion fue exitoso
        }
        return false; //se retorna falso cuando no existe la bdd
    }

    public boolean agregarUsuario(String nombre, String direccion, String email, String password, String tipo){
        SQLiteDatabase miBdd = getWritableDatabase(); // llamando a la base de datos en el objeto miBdd
        if(miBdd!=null){// validando que existan datos en la base de datos
            miBdd.execSQL("insert into usuario(nombre_usu,direccion_usu, email_usu, password_usu, tipo_usu) " +
                    "values('"+nombre+"','"+direccion+"','"+email+"','"+password+"','"+tipo+"')");//ejecutando la sentencia de insercion sql
            miBdd.close(); //cerrando la conexion a la base de datos
            return true; //retorno cuando la insercion es exitosa
        }
        return false;// retorno cuando no exista la base de datos
    }

    public Cursor obtenerUsuarioPorEmailPassword(String email, String password){
        SQLiteDatabase miBdd = getWritableDatabase(); // llamando a la base de datos en el objeto miBdd
        //Ejecutando la consulta y almacenando las resultados en el objeto usuario
        Cursor usuario= miBdd.rawQuery("select * from usuario where email_usu='"+email+"'\n" +
                "and password_usu='"+password+"';", null);
        if(usuario.moveToFirst()){ //verificando que el objeto usuario tenga resultados
            return usuario; //retornamos los datos encontrados

        }else{
            //No se encuentra el usuario -> porque no existe o porque el email y contrase;a ingresados son incorrectos
            return null;
        }

    }

    //vender productos Carrito de Compras***********************************************Sandoval***********************


    //Crear una Nueva Venta
    public boolean crearVenta(String fecha, Double total, String estado, Integer usuario){
        SQLiteDatabase miBdd =getWritableDatabase();
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into venta(fecha_ven, total_ven, estado_ven, fk_id_usu)" +
                    "values  ('"+fecha+"','"+total+"','"+estado+"','"+usuario+"');");
            //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }

    //Metodo para consultar la ultima venta creada
    public Cursor obtenerUltimaVenta(){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        //consultando las ventas en la base de datos y guardando en un cursor
        Cursor venta=miBdd.rawQuery("select * from venta where id_ven = (select max(id_ven) from venta);", null);
        if (venta.moveToFirst()){ //validar si se encontro o no datos
            miBdd.close();
            //retornar el cursor que contiene el listado de jugadores
            return venta; // retornar el cursor que contiene la ultima venta registrada
        }else{
            return null; //se retorna nulo cuando no hay la venta dentro de la tabla
        }
    }

    //Metodo para consultar venta con el id del usuario
    public Cursor obtenerVenta(String estado){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        //consultando los productos en la base de datos y guardando en un cursor
        Cursor venta_cliente=miBdd.rawQuery("select * from venta where estado_ven= '" +estado+"' ;", null);
        if (venta_cliente.moveToFirst()){ //validar si se encontro o no ventas
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return venta_cliente; // retornar el cursor que contiene el listado de productos
        }else{
            return null; //se retorna nulo cuando no hay productos dentro de la tabla
        }
    }

    //Metodo para consultar productos existentes en la venta
    public Cursor obtenerProductosVenta(Integer idVenta){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        //consultando los productos en la base de datos y guardando en un cursor
        Cursor productos_carrito=miBdd.rawQuery("select * from detalle_pedido_venta where fk_id_venta= '" +idVenta+"' ;", null);
        if (productos_carrito.moveToFirst()){ //validar si se encontro o no clientes
            miBdd.close();
            //retornar el cursor que contiene el listado de productos de la venta
            return productos_carrito; // retornar el cursor que contiene el listado de productos
        }else{
            return null; //se retorna nulo cuando no hay productos dentro de la tabla
        }
    }

    //metodo para actualizar
    public boolean actualizarVentaId(String fecha, Double total, String estado, Integer idUsuario, Integer id){
        SQLiteDatabase miBdd = getWritableDatabase(); // objeto para manejar la base de datos
        if(miBdd != null){
            //proceso de actualizacion
            miBdd.execSQL("update venta set fecha_ven='"+fecha+"', " +
                    "total_ven='"+total+"', estado_ven='"+estado+"', " +
                    "fk_id_usu='"+idUsuario+"' where id_ven="+id);
            miBdd.close(); //cerrando la conexion coon la BDD
            return true; //retornamos verdero ya que el proceso de actulaicacion fue exitoso
        }
        return false; // se retorna falso cuando no existe la base de datos
    }


    //agregar un nuevo Producto al carrito
    public boolean agregarProductoAlCarrito(String producto, Double precio, Integer cantidad, Double subtotal, Integer usuario){
        SQLiteDatabase miBdd =getWritableDatabase();
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into detalle_venta(producto_det, precio_det, cantidad_det,subtotal_det, fk_id_usu) " +
                    "values  ('"+producto+"','"+precio+"','"+cantidad+"','"+subtotal+"','"+usuario+"');");
            //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }

    //Metodo para consultar productos existentes en el carrito
    public Cursor obtenerProductosCarrito(Integer idUsuario){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        //consultando los productos en la base de datos y guardando en un cursor
        Cursor productos_carrito=miBdd.rawQuery("select * from detalle_venta where fk_id_usu= '" +idUsuario+"' ;", null);
        if (productos_carrito.moveToFirst()){ //validar si se encontro o no clientes
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return productos_carrito; // retornar el cursor que contiene el listado de productos
        }else{
            return null; //se retorna nulo cuando no hay productos dentro de la tabla
        }
    }

    public boolean eliminarProductoCarrito(String id){
        SQLiteDatabase miBdd = getWritableDatabase(); // objeto para manejar la base de datos
        if(miBdd != null){ //validando que la bdd realmente exista
            miBdd.execSQL("delete from detalle_venta where id_det="+id); //ejecucion de la sentencia Sql para eliminar
            miBdd.close();
            return true; // //retornamos verdero ya que el proceso de eliminacion fue exitoso
        }
        return false; // se retorna falso cuando no existe la base de datos
    }


    //agregar un nuevo Producto al carrito

    public boolean agregarProductoPedidoVenta(String producto, Double precio, Integer cantidad, Double subtotal, Integer venta){
        SQLiteDatabase miBdd =getWritableDatabase();
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into detalle_pedido_venta(producto_det_ped, precio_det_ped, cantidad_det_ped,subtotal_det_ped, fk_id_venta) " +
                    "values  ('"+producto+"','"+precio+"','"+cantidad+"','"+subtotal+"','"+venta+"');");
            //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }

    public Cursor obtenerProductoPorNombre(String nombre_pro) {
        SQLiteDatabase miBdd = getWritableDatabase(); // llamado a la base de datos
        //crear un cursor donde inserto la consulta sql y almaceno los resultados en el objeto usuario
        Cursor producto = miBdd.rawQuery("select * from producto where  nombre_prod='"+nombre_pro+"';", null);
        //validar si existe o no la consulta
        if (producto.moveToFirst()) { //metodo movetofirst nueve al primer elemento encontrado si hay el usuario
            return producto; //retornamos los datos encontrados
        } else {
            //no se encuentra informacion del producto -> no existe
            return null; //devuelvo null si no hay
        }
    }

    //Metodo para actualizar el stock del producto despachado
    public boolean actualizarStockProductoId(Integer stock, Integer id){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd
        if (miBdd != null){ //validando que la bdd realmente exista
            //proceso de actualizacion
            miBdd.execSQL("update producto set stock_prod='"+stock+"' where id_prod ="+id);
            miBdd.close(); //cerrando la conexion a la bdd
            return true; //retornando verdadero ya que el procesos de actualizacion fue exitoso
        }
        return false; //se retorna falso cuando no existe la bdd
    }

    //****************************************crud del Usuarios *************************
    public Cursor obtenerUsuarios(){
        SQLiteDatabase miBdd = getWritableDatabase(); //llamado a la bdd
        //consultando los usuarios en la BDD y guardandolos en un cursor
        Cursor usuarios=miBdd.rawQuery("select * from usuario", null);
        if(usuarios.moveToFirst()){ //verifica que el objeto tenga resultados
            miBdd.close(); //cerrando la conexion a la bdd
            return usuarios; //retorna el cursos que contiene el listado de clientes
        }else{
            return null; //retorna nulo cuando no hay productos dentro de la tabla
        }

    }
    //Metodo para consultar productos existentes en el carrito
    public Cursor obtenerUsuarioId(Integer idUsuario){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        //consultando los productos en la base de datos y guardando en un cursor
        Cursor cliente=miBdd.rawQuery("select * from usuario where id_usu = '" +idUsuario+"' ;", null);
        if (cliente.moveToFirst()){ //validar si se encontro o no clientes
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return cliente; // retornar el cursor que contiene el listado de productos
        }else{
            return null; //se retorna nulo cuando no hay productos dentro de la tabla
        }
    }

    public boolean eliminarUsuario(String id){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd

        if (miBdd != null){ //validando que la bdd realmente exista

            //proceso de eliminacion
            miBdd.execSQL("delete from usuario where id_usu="+id);
            miBdd.close(); //cerrando la conexion a la bdd
            return true; //retornando verdadero ya que el proceso de eliminacion fue exitoso
        }
        return false; //se retorna falso cuando no existe la bdd
    }

}
