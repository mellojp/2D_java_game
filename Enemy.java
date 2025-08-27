public class Enemy extends Entity{
    
    /**
     * Construtor da classe Enemy.
     * @param x A posição inicial do inimigo no eixo X.
     * @param y A posição inicial do inimigo no eixo Y.
     */
    public Enemy(int x, int y){
        super(x,y,3,"/sprites/enemy.png");
    }
    double hp = 3.0;
    /**
     * Atualiza a posição do inimigo para seguir o jogador.
     * @param p O jogador a ser seguido.
     */
    public void tick(Player p){
        int dx = p.getX_axis() - this.x_axis;
        int dy = p.getY_axis() - this.y_axis;
        double dist = Math.hypot(dx, dy);
        double velocityX = (dx / dist);
        double velocityY = (dy / dist);
        if(dist > 1){
            this.x_axis += velocityX * speed;
            this.y_axis += velocityY * speed;
        }
        this.area.x = this.x_axis;
        this.area.y = this.y_axis;
    }

    public void onHit(){
        this.hp = hp - 1;

    }

    public boolean checkDeath(){
        if(hp <= 0.0){
            return true;
        }else{
            return false;
        }
    }






}
