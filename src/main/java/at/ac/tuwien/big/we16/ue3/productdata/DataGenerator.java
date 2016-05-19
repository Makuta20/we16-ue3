package at.ac.tuwien.big.we16.ue3.productdata;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.api.SelectQueryBuilder;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPediaOWL;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.ProductType;
import at.ac.tuwien.big.we16.ue3.model.RelatedProduct;
import at.ac.tuwien.big.we16.ue3.model.User;
import at.ac.tuwien.big.we16.ue3.service.AuthService;
import at.ac.tuwien.big.we16.ue3.service.ProductService;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import org.h2.engine.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataGenerator {

    public void generateData() {
        generateUserData();
        generateProductData();
        insertRelatedProducts();
    }

    private void generateUserData() {
        // TODO add the computer user to the database
    }

    private ProductService productService = new ProductService();
    private void generateProductData() {

        // TODO load products via JSONDataLoader and write them to the database
        JSONDataLoader.Book[] books = JSONDataLoader.getBooks();
        JSONDataLoader.Music[] music = JSONDataLoader.getMusic();
        JSONDataLoader.Movie[] movies = JSONDataLoader.getFilms();

        for(JSONDataLoader.Book b : books){
            Product p = new Product();

            p.setName(b.getTitle());
            p.setProducer(b.getAuthor());
            p.setYear(Integer.parseInt(b.getYear()));
            p.setImage(b.getImg());
            p.setImageAlt(b.getImg());
            p.setType(ProductType.BOOK);

            productService.createProduct(p);
        }

        for(JSONDataLoader.Music m : music){
            Product p = new Product();

            p.setName(m.getAlbum_name());
            p.setProducer(m.getArtist());
            p.setYear(Integer.parseInt(m.getYear()));
            p.setImage(m.getImg());
            p.setImageAlt(m.getImg());
            p.setType(ProductType.ALBUM);

            productService.createProduct(p);
        }

        for(JSONDataLoader.Movie m : movies){
            Product p = new Product();

            p.setName(m.getTitle());
            p.setProducer(m.getDirector());
            p.setYear(Integer.parseInt(m.getYear()));
            p.setImage(m.getImg());
            p.setImageAlt(m.getImg());
            p.setType(ProductType.FILM);

            productService.createProduct(p);
        }


    }


    private void insertRelatedProducts() {
        // TODO load related products from dbpedia and write them to the database



        // TODO verify and complete (review) code for loading Products from dbpedia
        if(!DBPediaService.isAvailable())
            return;

        for(Product p : productService.getAllProducts()){
            Resource producer = DBPediaService.loadStatements(DBPedia.createResource(p.getProducer()));

            String enlishProducerName = DBPediaService.getResourceName(producer, Locale.ENGLISH);
            String germanProducerName = DBPediaService.getResourceName(producer, Locale.GERMAN);

            SelectQueryBuilder query = DBPediaService.createQueryBuilder();
            query.setLimit(5);

            //fix type
            if(p.getType().equals(ProductType.ALBUM)){
                query.addWhereClause(RDF.type, DBPediaOWL.Album);
            }else if(p.getType().equals(ProductType.BOOK)){
                query.addWhereClause(RDF.type, DBPediaOWL.Book);
            }else if(p.getType().equals(ProductType.FILM)){
                query.addWhereClause(RDF.type, DBPediaOWL.Film);
            }

            query.addFilterClause(RDFS.label, Locale.ENGLISH)
                    .addFilterClause(RDFS.label, Locale.GERMAN);

            Model relatedProducts = DBPediaService.loadStatements(query.toQueryString());

            List<String> englishRelatedProducts = DBPediaService.getResourceNames(relatedProducts, Locale.ENGLISH);
            //List<String> germanRelatedProducts = DBPediaService.getResourceNames(relatedProducts, Locale.GERMAN);

            for(String s : englishRelatedProducts){
                RelatedProduct relatedProduct = new RelatedProduct();
                relatedProduct.setName(s);
                relatedProduct.setProduct(p);
            }
        }
    }
}
