package co.id.rezha.mycrud.views.sqlite.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import co.id.rezha.mycrud.views.sqlite.data.models.Product

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "shop.db"
        private const val DATABASE_VERSION = 1


        // Tabel Products
        const val TABLE_PRODUCTS = "products"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "nama"
        const val COLUMN_STOCK = "stok"
        const val COLUMN_PRICE = "harga"
        const val COLUMN_IMAGE_PATH = "path_image"

        // Tabel Cart
        const val TABLE_CART = "keranjang"
        const val COLUMN_CART_ID = "id"
        const val COLUMN_PRODUCT_ID = "produk_id"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_DATE_ADDED = "date_add"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createProductsTable = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_STOCK INTEGER NOT NULL,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_IMAGE_PATH TEXT
            )
        """.trimIndent()

        val createCartTable = """
            CREATE TABLE $TABLE_CART (
                $COLUMN_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PRODUCT_ID INTEGER NOT NULL,
                $COLUMN_QUANTITY INTEGER NOT NULL,
                $COLUMN_DATE_ADDED TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_PRODUCT_ID) REFERENCES $TABLE_PRODUCTS($COLUMN_ID)
            )
        """.trimIndent()

        db?.execSQL(createProductsTable)
        db?.execSQL(createCartTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    // CRUD Operations for Products
    fun insertProduct(product: Product): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_STOCK, product.stock)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_IMAGE_PATH, product.imagePath)
        }
        return db.insert(TABLE_PRODUCTS, null, values)
    }

    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PRODUCTS,
            null, null, null, null, null, null
        )

        cursor.use {
            while (it.moveToNext()) {
                val product = Product(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    stock = it.getInt(it.getColumnIndexOrThrow(COLUMN_STOCK)),
                    price = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PRICE)),
                    imagePath = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGE_PATH))
                )
                products.add(product)
            }
        }
        return products
    }

    fun updateProduct(product: Product): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_STOCK, product.stock)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_IMAGE_PATH, product.imagePath)
        }
        return db.update(
            TABLE_PRODUCTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(product.id.toString())
        )
    }

    fun deleteProduct(id: Long): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_PRODUCTS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getProductById(id: Long): Product? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PRODUCTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                Product(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    stock = it.getInt(it.getColumnIndexOrThrow(COLUMN_STOCK)),
                    price = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PRICE)),
                    imagePath = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGE_PATH))
                )
            } else {
                null
            }
        }
    }


}