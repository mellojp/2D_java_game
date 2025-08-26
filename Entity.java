public class Entity {

    protected int x_axis;
    protected int y_axis;

    public Entity(){
        this.x_axis = 0;
        this.y_axis = 0;
    }

    public int getX_axis(){
        return this.x_axis;
    }

    public int getY_axis(){
        return this.y_axis;
    }

    public void setX_axis(int value){
        this.x_axis = value;
    }

    public void setY_axis(int value){
        this.y_axis = value;
    }
}