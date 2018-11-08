package com.gurjinder.tandooriBackend.DAOs;

import com.gurjinder.tandooriBackend.model.FoodCategory;
import com.gurjinder.tandooriBackend.model.FoodItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

//import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class
FoodItemAndCategoryDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<FoodItem>  getAllFoodIItems(){

      return jdbcTemplate.query("select * from FOOD_ITEMS ft,ITEM_CATEGORIES  ic where ft.ID=ic.FOOD_ITEM_ID",
              new ResultSetExtractor<List<FoodItem>>() {
                  @Override
                  public List<FoodItem> extractData(ResultSet rs) throws SQLException, DataAccessException                        {
                      List<FoodItem> foodItems=new ArrayList<>();
                      FoodItem foodItem=null;

                      while(rs.next()){
                        if (foodItem==null) {

                            foodItem=new FoodItem();

                        }
                        if(foodItem.getId()!=rs.getInt("id")){
                           int itemId =rs.getInt("id");
                           String itemName=rs.getString("name");
                           Double price=rs.getDouble("price");
                           String description=rs.getString("description");
                           int availability=rs.getInt("availability");
                           int categoryId=rs.getInt("category_id");

                           foodItem.setId(itemId);
                           foodItem.setName(itemName);
                           foodItem.setPrice(price);
                           foodItem.setDescription(description);
                           foodItem.setCategoryIds(new ArrayList<>());
                           foodItem.getCategoryIds().add(categoryId);
                           if(availability==0){
                               foodItem.setAvailabily(false);
                           }
                           else{
                               foodItem.setAvailabily(true);
                           }
                           try {
                               foodItems.add((FoodItem) foodItem.clone());
                           }
                           catch( CloneNotSupportedException c){

                           }
                        }
                        else if(foodItem.getId()==rs.getInt("id")){
                            foodItem.getCategoryIds().add(rs.getInt("category_id"));
                        }


                      }
                      return foodItems;
                  }
              });

  }


    public List<FoodCategory> getAllCategories(){
        return jdbcTemplate.query("select * from categories",
                new BeanPropertyRowMapper<FoodCategory>(FoodCategory.class));
    }



    // admin specific
    public FoodCategory addFoodCategory(FoodCategory  category){
        int maxCategoryId;
        try{
            maxCategoryId=jdbcTemplate.queryForObject("select max(id) from categories",Integer.class);

        }
        catch (EmptyResultDataAccessException e){
            maxCategoryId=0;

        }
        catch(NullPointerException e){
            maxCategoryId=0;
        }
        category.setId(maxCategoryId+1);
        String insertStatement="insert into categories(id,name,description) values(?,?,?)";

        jdbcTemplate.update(insertStatement,new Object[]{category.getId(),category.getName(),category.getDescription()});

        return category;

    }

    public FoodItem addFoodItem(FoodItem foodItem){

        int maxItemId;
        try{
            maxItemId=jdbcTemplate.queryForObject("select max(id) from food_items",Integer.class);

        }
        catch (EmptyResultDataAccessException e){
            maxItemId=0;

        }
        catch(NullPointerException e){
            maxItemId=0;
        }
        foodItem.setId(maxItemId+1);
        String insertStatement="insert into food_Items(id,name,price,description) values(?,?,?,?)";
        jdbcTemplate.update(insertStatement,new Object[]{foodItem.getId(),foodItem.getName(),
                foodItem.getPrice(),foodItem.getDescription()});
        insertStatement="insert into item_categories(food_item_id,category_id) values(?,?)";
        jdbcTemplate.batchUpdate(insertStatement, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1,foodItem.getId());
                ps.setInt(2,foodItem.getCategoryIds().get(i));
            }

            @Override
            public int getBatchSize() {
                return foodItem.getCategoryIds().size();
            }
        });
        return foodItem;
    }







    //test
    public String getbyid(){
        return (String)jdbcTemplate.queryForObject("select name from categories where id=?",new  Object[]{1},String.class);
    }


    public void  test() {
     // jdbcTemplate.execute("drop table categories");
    }
}
