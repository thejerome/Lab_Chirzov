package vlab.server_java.model;

import java.awt.*;
import java.awt.geom.*;
import java.math.BigDecimal;

class Ustanovka{
    //настройка
    private int uWidth = 620, uHeight = 450, //Высота и ширина установки
            topFieldHeight = 50, //Высота верхнего поля установки
            uBorder = 15, //ширина бордюра
            settingStepenSvobodi = 0;
    private boolean  isMove = true,isNoMoveStop = false;

    private Font fontNormal;
    BasicStroke penNormal = new BasicStroke(1.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,20.0f);
    private FontMetrics fontMetricsNormal;
    private Color uColorBorder = new Color(165,216,254), //цвет рамки
            uColorField = new Color(234,247,255), //цвет поля установки
            uColorLine = new Color(2,101,174); //цвет линий
    //переменные шкалы
    private int[] massShkala = {-2,-1,0,1,2};
    public int widthShkala = 30,//ширина между рисками
            zeroHeightShkala = 10, maxHeightShkala = zeroHeightShkala/2, minHeightShkala = maxHeightShkala/2,//высота рисок ноль, чисел, половины числа соответственно
            zerox0Shkala = uWidth/2+50, zeroy0Shkala = topFieldHeight + (int)(1.5*uBorder);//координаты риски ноль
    //переменные детали 1
    public double r1 = 0.6*widthShkala;
    double  t = 0.0, tMax = 10, deltax = 0, l=10.8*r1, fi0=0, fi=fi0,r = l/2, i2=widthShkala*0.7, V=0, omega=0,step = 0.01;
    double widthWater = uWidth - 1.5*uBorder - massShkala[massShkala.length-1]*widthShkala - zerox0Shkala - 2.5*r1;//длина контейнера с водой
    double m11 = 0.2, m12 = 8.0, m21 = 0.3, m22 = 2,
            k1 = 200, k2 = 200, //жесткость пружины
            k3 = 0.1, k4 = 0.01, k5 = 0.1, // коэф. трения демфера, подвески, маятника
            r3 = 1.0*widthShkala, p=2;
    String tempText="";


    public Ustanovka ( double t){

    }

    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D)g;
        paintBorders(g);
        painDetal1(g2D,zerox0Shkala,topFieldHeight + uBorder,widthShkala,r1*2);
    }

    public void update(Graphics g){

    }
    //фон
    private void paintBorders(Graphics g) {
        String str = "L = " + get_l() + ", r1 = " + get_r1() + ", i1 = " + 0.01*Math.round(get_i1()*100),
                strTime = "T = " + getT();
        strTime = strTime + "00000"; strTime  = strTime.substring(0,5 + strTime.indexOf('.'));

        fontNormal = new Font("sansSerif",Font.PLAIN,11);
        fontMetricsNormal = g.getFontMetrics(fontNormal);
        g.setColor(uColorBorder);
        g.fillRect(0, 0, uWidth, uHeight);
        g.setColor(uColorField);
        g.fillRect(uBorder, uBorder, uWidth - 2*uBorder, topFieldHeight);
        g.fillRect(uBorder,3*uBorder+topFieldHeight, uWidth - 2*uBorder, uHeight - topFieldHeight - 4*uBorder);
        paintLineyka(g);
        g.drawString(str,2*uBorder,uHeight - 2*uBorder);
        g.drawString(strTime,uWidth - 2*uBorder - 50,uHeight - 2*uBorder);
    }
    //--> линейка-------------------------------------------------------------------
    private void paintLineyka(Graphics g) {
        g.setColor(uColorLine);
        paintLineykaZero(g);
        for (int i=0; i < massShkala.length; i++) {paintLineykaMax(g,massShkala[i]);}
        for (int i=0; i < massShkala.length-1; i++) {paintLineykaMin(g,massShkala[i]);}
    }
    private void paintLineykaZero(Graphics g){
        g.drawLine(zerox0Shkala,(int)(zeroy0Shkala-0.5 * zeroHeightShkala),zerox0Shkala,(int)(zeroy0Shkala+0.5*zeroHeightShkala));
    }
    private void paintLineykaMax(Graphics g,int number){
        String str = ""+number;
        int textX = zerox0Shkala + (int)(number*widthShkala);
        if (number > 0) {textX = textX - (int)(0.5 * fontMetricsNormal.stringWidth(str));}
        else  {textX = textX - (int)(fontMetricsNormal.stringWidth(str)/2);}
        g.drawLine(zerox0Shkala + number*widthShkala,(int)(zeroy0Shkala-0.5*maxHeightShkala),zerox0Shkala + number*widthShkala,(int)(zeroy0Shkala+0.5*maxHeightShkala));
        g.drawString(str,textX ,(int)(zeroy0Shkala+0.5*maxHeightShkala+fontMetricsNormal.getHeight()));

    }
    private void paintLineykaMin(Graphics g,int number){
        g.drawLine((int)(zerox0Shkala + number*widthShkala + widthShkala/2),(int)(zeroy0Shkala-minHeightShkala),(int)(zerox0Shkala + number*widthShkala + widthShkala/2),(int)(zeroy0Shkala+0.5*minHeightShkala));
    }
    //<-- линейка-------------------------------------------------------------------
//--> деталь 1------------------------------------------------------------------
    private void painDetal1(Graphics2D g,int x,int y,int zoom,double d){

        int deltaRound = 1;
        g.setStroke(penNormal);
        painCircleR1(g,(int)(x+deltax*zoom-d),(int)(y-0.5*d),d,deltax*zoom);
        painCircleR1(g,(int)(x+deltax*zoom+d),(int)(y-0.5*d),d,deltax*zoom);
        painCircleR1(g,(int)(x+deltax*zoom),(int)(y+2*uBorder+0.5*d),d,deltax*zoom);
        GeneralPath gp = new GeneralPath();
        Area area1 = new Area(new RoundRectangle2D.Float((float)(x+deltax*zoom-1.25*d),(float)(y-0.75*d), (float)(2.5*d),(float)(0.5*d),10,10));
        Area area2 = new Area(new Rectangle2D.Float((float)(x+deltax*zoom-0.25*d),(float)(y-0.25*d), (float)(0.5*d),(float)(y+2 * uBorder + 1.25 * d)));
        Area area3 = new Area(new RoundRectangle2D.Double((double)(x+deltax*zoom-1.25*d),(double)(y+2 * uBorder + 1.25 * d), (double)(2.5*d),(double)(2.5*d),10,10));
        area1.add(area2); area1.add(area3);
        gp.append(area1,false);
        //water
        paintWater(g,(y+2 * uBorder + 2.25 * d));
        gp.append(new Rectangle2D.Double((double)(x+deltax*zoom+1.25*d),(double)(y+2 * uBorder + 2.25 * d - 2), (double)widthWater-5,4.0),false);
        gp.append(new Rectangle2D.Double((double)(x+deltax*zoom+1.25*d + widthWater-5),(double)(y+2 * uBorder + 2.25 * d - 9),5.0,17.0),false);

        g.setColor(uColorField);
        g.fill(gp);
        g.setColor(uColorLine);
        g.draw(gp);

        painCircleR2(g,(int)(x+deltax*zoom-d),(int)(y-0.5*d),2);
        painCircleR2(g,(int)(x+deltax*zoom+d),(int)(y-0.5*d),2);
        painCircleR2(g,(int)(x+deltax*zoom),(int)(y+2*uBorder+0.5*d),2);

        painPruzgina(g,uBorder, (int)(y-0.5*d), (int)(x+deltax*zoom-d),(int)(y-0.5*d), 21, 40);
        paintMotor(g,(x+deltax*zoom),(y+2 * uBorder + 2.25 * d));

        paintMayatnik(g,(x+deltax*zoom),(y+2 * uBorder + 3 * d));
    }
    private void painCircleR1(Graphics2D g,int x0, int y0, double d, double deltax){
        double r = 0.5*d;
        double alpha = deltax/r;
        g.drawOval((int)(x0-r-1),(int)(y0-r-1),(int)d,(int)d);
        g.draw( new Line2D.Double((double)(x0 + r*Math.cos(alpha)),(double)(y0+ r*Math.sin(alpha)),(double)(x0+ r*Math.cos(Math.PI+alpha)),(double)(y0+ r*Math.sin(Math.PI+alpha))) );
        g.draw( new Line2D.Double((double)(x0 + r*Math.cos(0.5*Math.PI+alpha)),(double)(y0+ r*Math.sin(0.5*Math.PI+alpha)),(double)(x0+ r*Math.cos(1.5*Math.PI+alpha)),(double)(y0+ r*Math.sin(1.5*Math.PI+alpha))) );
        g.draw( new Line2D.Double((double)(x0 + r*Math.cos(0.25*Math.PI+alpha)),(double)(y0+ r*Math.sin(0.25*Math.PI+alpha)),(double)(x0+ r*Math.cos(1.25*Math.PI+alpha)),(double)(y0+ r*Math.sin(1.25*Math.PI+alpha))) );
        g.draw( new Line2D.Double((double)(x0 + r*Math.cos(0.75*Math.PI+alpha)),(double)(y0+ r*Math.sin(0.75*Math.PI+alpha)),(double)(x0+ r*Math.cos(1.75*Math.PI+alpha)),(double)(y0+ r*Math.sin(1.75*Math.PI+alpha))) );
    }
    private void painCircleR2(Graphics2D g,double x0, double y0, double d){
        double r = 0.5*d;
        g.setColor(uColorField);
        g.fillOval((int)(x0-r-1),(int)(y0-r-1),(int)d,(int)d);
        g.setColor(uColorLine);
        g.drawOval((int)(x0-r-1),(int)(y0-r-1),(int)d,(int)d);
    }
    //пружина
    private void painPruzgina(Graphics2D g,double xleft, double yleft, double xright, double yright, int count, int dlinaSayd){
        int dlina = 20;
        double alpha = Math.atan((yleft - yright)/(xright - xleft)), //угол поворота пружины
                dlinaOX = Math.sqrt((xright-xleft)*(xright-xleft) + (yright-yleft)*(yright-yleft)),
                xleftNew = xright - dlinaOX,
                yleftNew = yright,
                dx = (dlinaOX - 2*dlinaSayd)/(count+1),
                x0 = xleftNew + (dlinaSayd + 0.5*dx),
                beta = Math.acos(dx/dlina);
        float x1,x2,y1,y2,y0;
        AffineTransform at =  new AffineTransform();
        g.setStroke(penNormal);
        GeneralPath gp = new GeneralPath();
        gp.moveTo((float)(xleftNew),(float)(yleftNew));
        gp.lineTo((float)(xleftNew + dlinaSayd),(float)(yleftNew));

        for (int i = 0; i < Math.round(0.5*count); i++) {
            x1 = (float)(i * 2 * dx + x0);  y1 = (float)(yright - 0.5 * dlina * (Math.sin(beta)));
            x2 = (float)(x1 + dx); y2 = (float)(yright + 0.5 * dlina * Math.sin(beta));
            gp.lineTo(x1,y1); gp.lineTo(x2,y2);
        }

        gp.lineTo((float)(xright-dlinaSayd),(float)(yright));
        gp.lineTo((float)xright,(float)yright);
        at.setToRotation(-alpha,xright,yright);
        gp.createTransformedShape(at);
        gp.transform(at);
        g.draw(gp);

    }
    //отрисовка мотора с прилегающей пружиной
    private void paintMotor(Graphics2D g,double x,double y){
        AffineTransform at =  new AffineTransform();
        g.setStroke(penNormal);
        g.drawRect((int)(uBorder),(int)(y-20),10,40);
        g.drawRect((int)(uBorder+10),(int)(y-10),10,20);
        painCircleR2(g,20+uBorder+widthShkala,y,2*widthShkala);
        painCircleR2(g,20+uBorder+widthShkala,y,widthShkala);

        GeneralPath gp = new GeneralPath();
        gp.append(new RoundRectangle2D.Double((double)(20+uBorder+widthShkala - r3 - 5),(double)(y-5),(double)(r3 + 10),(double)(11),10,10), false);
        gp.append(new Ellipse2D.Double((double)(20+uBorder+widthShkala - r3),(double)(y-2),(double)(4),(double)(4)), false);
        gp.moveTo((float)(20+uBorder+widthShkala - r3), (float)(y));
        double alpha = -getT()*get_p()+Math.PI/2;
        at.setToRotation(alpha,20+uBorder+widthShkala,y);
        gp.createTransformedShape(at);
        gp.transform(at);
        g.setColor(uColorField);
        g.fill(gp);
        g.setColor(uColorLine);
        painCircleR2(g,20+uBorder+widthShkala,y,2);
        g.draw(gp);
        painCircleR2(g,x,y,8);
        painCircleR2(g,x,y,2);
        painPruzgina(g,(int)gp.getCurrentPoint().getX(), (int)gp.getCurrentPoint().getY(), (int)(x),(int)(y), 13, 70);
    }

    //отрисовка маятника
    private void paintMayatnik(Graphics2D g,double x,double y){
        AffineTransform at =  new AffineTransform();
        g.setStroke(penNormal);
        GeneralPath gp = new GeneralPath();
        Area area1 = new Area(new RoundRectangle2D.Double((double)(x-3),(double)(y-3),(double)(7),(double)(3+l),7,7));
        Area area2 = new Area(new Ellipse2D.Double((double)(x-Math.sqrt(2)*i2),(double)(y+r-Math.sqrt(2)*i2),(double)(2*Math.sqrt(2)*i2),(double)(2*Math.sqrt(2)*i2)));
        Area area3 = new Area(new Ellipse2D.Double((double)(x-2),(double)(y+r-2),(double)(3),(double)(3)));
        Area area4 = new Area(new Ellipse2D.Double((double)(x-2),(double)(y-2),(double)(3),(double)(3)));
        area1.add(area2);
        gp.append(area1,false); gp.append(area2,false); gp.append(area3,false);	gp.append(area4,false);
        at.setToRotation(-fi,x,y);
        gp.createTransformedShape(at);
        gp.transform(at);
        g.setColor(uColorField);
        g.fill(gp);
        g.setColor(uColorLine);
        g.draw(gp);
    }
    //отрисовка воды
    private void paintWater(Graphics2D g, double y){
        g.setStroke(penNormal);
        GeneralPath gp = new GeneralPath();
        g.setColor(uColorBorder);
        gp.append(new Rectangle2D.Float((float)(uWidth - 1.5*uBorder - widthWater + 3),(float)(y-12),(float)(widthWater-4),24.0f), false);
        g.fill(gp);
        g.setColor(uColorLine);
        gp.append(new Rectangle2D.Double((uWidth - 1.5*uBorder),(y-20),(0.5*uBorder),40), false);
        gp.moveTo((float)(uWidth - 1.5*uBorder),(float)(y-15));
        gp.lineTo((float)(uWidth - 1.5*uBorder - widthWater),(float)(y-15));
        gp.lineTo((float)(uWidth - 1.5*uBorder - widthWater),(float)(y-3));
        gp.lineTo((float)(uWidth - 1.5*uBorder - widthWater + 3),(float)(y-3));
        gp.lineTo((float)(uWidth - 1.5*uBorder - widthWater + 3),(float)(y-12));
        gp.lineTo((float)(uWidth - 1.5*uBorder),(float)(y-12));

        gp.moveTo((float)(uWidth - 1.5*uBorder),(float)(y+15));
        gp.lineTo((float)(uWidth - 1.5*uBorder - widthWater),(float)(y+15));
        gp.lineTo((float)(uWidth - 1.5*uBorder - widthWater),(float)(y+3));
        gp.lineTo((float)(uWidth - 1.5*uBorder - widthWater + 3),(float)(y+3));
        gp.lineTo((float)(uWidth - 1.5*uBorder - widthWater + 3),(float)(y+12));
        gp.lineTo((float)(uWidth - 1.5*uBorder),(float)(y+12));

        g.draw(gp);

    }
    //<-- деталь 1------------------------------------------------------------------
//настройки
    public void setuWidth(int w){
        uWidth = w;
    }
    public void setuHeight(int h){
        this.uHeight = h;
    }
    public void setDeltax(double deltax){
        this.deltax = deltax;
    }
    public void setT(double t){
        this.t = t;
    }
    public void setTMax(double t){
        this.tMax = t;
    }
    public void set_isMove(boolean w){
        this.isMove = w;
    }
    public void set_isNoMoveStop(boolean w){
        this.isNoMoveStop = w;
    }
    public void set_fi0(double v){
        this.fi = v*Math.PI/180;
    }

    public void set_fi(double v){
        this.fi = v;
    }

    public void set_m11(double v){
        this.m11 = v;
    }

    public void set_m12(double v){
        this.m12 = v;
    }
    public void set_m21(double v){
        this.m21 = v;
    }
    public void set_m22(double v){
        this.m22 = v;
    }
    public void set_r(double v){
        this.r = v*widthShkala;
    }
    public void set_i2(double v){
        this.i2 = v*widthShkala;
    }

    public void set_k1(double v){
        this.k1 = v;
    }
    public void set_k2(double v){
        this.k2 = v;
    }
    public void set_k3(double v){
        this.k3 = v;
    }
    public void set_k4(double v){
        this.k4 = v;
    }
    public void set_k5(double v){
        this.k5 = v;
    }
    public void set_r3(double v){
        this.r3 = v*widthShkala;
    }
    public void set_p(double v){
        this.p = v;
    }
    public void set_V(double v){
        this.V = v;
    }
    public void set_omega(double v){
        this.omega = v;
    }
    public void set_step(double v){
        this.step = v;
    }
    public void set_settingStepenSvobodi(int v){
        this.settingStepenSvobodi = v;
    }

    public double getDeltax(){
        return deltax;
    }
    public double getT(){
        return t;
    }
    public double getTMax(){
        return tMax;
    }
    public boolean get_isMove(){
        return isMove;
    }
    public boolean get_isNoMoveStop(){
        return isNoMoveStop;
    }
    public double get_fi0(){
        return fi0;
    }

    public double get_fi(){
        return fi;
    }

    public double get_m11(){
        return m11;
    }

    public double get_m12(){
        return m12;
    }
    public double get_m21(){
        return m21;
    }
    public double get_m22(){
        return m22;
    }
    public double get_r(){
        return r/widthShkala;
    }
    public double get_r1(){
        return r1/widthShkala;
    }
    public double get_i1(){
        return get_r1()/Math.sqrt(2);
    }
    public double get_i2(){
        return i2/widthShkala;
    }

    public double get_k1(){
        return k1;
    }
    public double get_k2(){
        return k2 ;
    }
    public double get_k3(){
        return k3 ;
    }
    public double get_k4(){
        return k4 ;
    }
    public double get_k5(){
        return k5 ;
    }
    public double get_r3(){
        return r3/widthShkala;
    }
    public double get_p(){
        return p;
    }
    public double get_l(){
        return l/widthShkala;
    }
    public double get_V(){
        return V;
    }
    public double get_omega(){
        return omega;
    }
    public double get_step(){
        return step;
    }

    public int get_settingStepenSvobodi(){
        return settingStepenSvobodi;
    }
    public String get_tempText(){
        return tempText;
    }
    public void add_tempText(String v){
        tempText = tempText + v;
    }
    public void clear_tempText(){
        tempText = "";
    }

    public String strDbl(double d) {
        String s = "" + d + "0000000";
        s  = s.substring(0,5 + s.indexOf('.'));
        return s;
    }

    public void newPolozenie(){
        switch (get_settingStepenSvobodi()) {
            case 1: RungeKutte4_1(); break;
            case 2: RungeKutte4_2(); break;
            default: RungeKutte4();
        }
        String tab = "      ";
        add_tempText(tab + strDbl(getT()) + tab + strDbl(get_fi()) + tab + strDbl(getDeltax()) + tab + strDbl(get_V()) + tab + strDbl(get_omega()) + "\r\n");


    }
    public void Eiler(){
        double grav = 9.8;
        double
                a11 = get_m12() + get_m21() + get_m22() + 0.5*grav* get_m11(),
                a12 = get_m21() * get_r1() + get_m22() * get_r(),
                a22 = get_m21() * Math.pow(get_l(),2)/3 + get_m22() * (Math.pow(get_i2(),2) + Math.pow(get_r(),2)),
                C1 = get_k1() + get_k2(),
                h1 = get_k2() * get_r3(),
                C2 = get_m21() * grav * get_l()/2 + get_m22() * grav * get_r(),
                delt = a11 * a22 - Math.pow(a12,2) * Math.pow(Math.cos(get_fi()),2),
                Q1 = - ((get_k3() + get_k4()) * get_V() + C1 * getDeltax()) - h1*Math.sin(get_p() * getT()),
                Q2  = -get_k5() * get_omega() - C2 * Math.sin(get_fi()),
                deltaV = (1/delt) * (a22 * Q1 + a12 * a22 * Math.sin(get_fi()) * Math.pow(get_omega(),2) - Q2*a12*Math.cos(get_fi())),
                deltaOmega = (1/delt) * (a11 * Q2 - a12 * Q1 * Math.cos(get_fi()) - Math.pow(a12,2)*Math.pow(get_omega(),2)*Math.sin(get_fi())*Math.cos(get_fi()) ),
                deltaX = get_V(),
                deltaFi = get_omega();

        setT(getT() + get_step());
        set_V(get_V() + get_step()*deltaV);
        set_omega(get_omega() + get_step()*deltaOmega);
        setDeltax(getDeltax() + get_step()*deltaX);
        set_fi(get_fi() + get_step()*deltaFi);

    }


    public void Eiler_Koshi(){
        double grav = 9.8;
        double
                a11 = get_m12() + get_m21() + get_m22() + 0.5*grav* get_m11(),
                a12 = get_m21() * get_r1() + get_m22() * get_r(),
                a22 = get_m21() * Math.pow(get_l(),2)/3 + get_m22() * (Math.pow(get_i2(),2) + Math.pow(get_r(),2)),
                C1 = get_k1() + get_k2(),
                h1 = get_k2() * get_r3(),
                C2 = get_m21() * grav * get_l()/2 + get_m22() * grav * get_r(),

                delt = a11 * a22 - Math.pow(a12,2) * Math.pow(Math.cos(get_fi()),2),
                Q1 = - ((get_k3() + get_k4()) * get_V() + C1 * getDeltax()) - h1*Math.sin(get_p() * getT()),
                Q2  = -get_k5() * get_omega() - C2 * Math.sin(get_fi()),
                deltaV = (1/delt) * (a22 * Q1 + a12 * a22 * Math.sin(get_fi()) * Math.pow(get_omega(),2) - Q2*a12*Math.cos(get_fi())),
                deltaOmega = (1/delt) * (a11 * Q2 - a12 * Q1 * Math.cos(get_fi()) - Math.pow(a12,2)*Math.pow(get_omega(),2)*Math.sin(get_fi())*Math.cos(get_fi()) ),
                deltaX = get_V(),
                deltaFi = get_omega();


        double t2 = getT() + get_step(),
                v2 = get_V() + get_step()*deltaV,
                omega2 = get_omega() + get_step()*deltaOmega,
                x2 = getDeltax() + get_step()*deltaX,
                fi2 = get_fi() + get_step()*deltaFi,

                delt_ = a11 * a22 - Math.pow(a12,2) * Math.pow(Math.cos(fi2),2),
                Q1_ = - ((get_k3() + get_k4()) * v2 + C1 * x2) - h1*Math.sin(get_p() * t2),
                Q2_  = -get_k5() * omega2 - C2 * Math.sin(fi2),
                deltaV_ = (1/delt_) * (a22 * Q1 + a12 * a22 * Math.sin(fi2) * Math.pow(omega2,2) - Q2*a12*Math.cos(fi2)),
                deltaOmega_ = (1/delt_) * (a11 * Q2 - a12 * Q1 * Math.cos(fi2) - Math.pow(a12,2)*Math.pow(omega2,2)*Math.sin(fi2)*Math.cos(fi2) ),
                deltaX_ = v2,
                deltaFi_ = omega2;

        setT(t2);
        set_V(get_V() + 0.5*get_step()*(deltaV+deltaV_));
        set_omega(get_omega() + 0.5*get_step()*(deltaOmega + deltaOmega_));
        setDeltax(getDeltax() + 0.5*get_step()*(deltaX + deltaX_));
        set_fi(get_fi() + 0.5*get_step()*(deltaFi + deltaFi_));

    }

    public void RungeKutte4(){
        double grav = 9.8;
        double
                a11 = get_m12() + get_m21() + get_m22() + 0.5*grav* get_m11(),
                a12 = get_m21() * get_r1() + get_m22() * get_r(),
                a22 = get_m21() * Math.pow(get_l(),2)/3 + get_m22() * (Math.pow(get_i2(),2) + Math.pow(get_r(),2)),
                C1 = get_k1() + get_k2(),
                h1 = get_k2() * get_r3(),
                C2 = get_m21() * grav * get_l()/2 + get_m22() * grav * get_r(),

                kn_1_delt = a11 * a22 - Math.pow(a12,2) * Math.pow(Math.cos(get_fi()),2),
                kn_1_Q1 = - ((get_k3() + get_k4()) * get_V() + C1 * getDeltax()) - h1*Math.sin(get_p() * getT()),
                kn_1_Q2  = -get_k5() * get_omega() - C2 * Math.sin(get_fi()),
                kn_1_deltaV = (1/kn_1_delt) * (a22 * kn_1_Q1 + a12 * a22 * Math.sin(get_fi()) * Math.pow(get_omega(),2) - kn_1_Q2*a12*Math.cos(get_fi())),
                kn_1_deltaOmega = (1/kn_1_delt) * (a11 * kn_1_Q2 - a12 * kn_1_Q1 * Math.cos(get_fi()) - Math.pow(a12,2)*Math.pow(get_omega(),2)*Math.sin(get_fi())*Math.cos(get_fi()) ),
                kn_1_deltaX = get_V(),
                kn_1_deltaFi = get_omega();


        double tn_h2 = getT() + 0.5*get_step(),
                get_fi_t2 = get_fi() + 0.5*get_step()*kn_1_deltaFi,
                get_V_t2 = get_V() + 0.5*get_step() * kn_1_deltaV,
                get_omega_t2 = get_omega() + 0.5*get_step() * kn_1_deltaOmega,
                getDeltax_t2 = getDeltax() + 0.5*get_step() * kn_1_deltaX,

                kn_2_delt = a11 * a22 - Math.pow(a12,2) * Math.pow(Math.cos(get_fi_t2),2),
                kn_2_Q1 = - ((get_k3() + get_k4()) * get_V_t2 + C1 * getDeltax_t2) - h1*Math.sin(get_p() * tn_h2),
                kn_2_Q2  = -get_k5() * get_omega_t2 - C2 * Math.sin(get_fi_t2),
                kn_2_deltaV = (1/kn_2_delt) * (a22 * kn_2_Q1 + a12 * a22 * Math.sin(get_fi_t2) * Math.pow(get_omega_t2,2) - kn_2_Q2*a12*Math.cos(get_fi_t2)),
                kn_2_deltaOmega = (1/kn_2_delt) * (a11 * kn_2_Q2 - a12 * kn_2_Q1 * Math.cos(get_fi_t2) - Math.pow(a12,2)*Math.pow(get_omega_t2,2)*Math.sin(get_fi_t2)*Math.cos(get_fi_t2) ),
                kn_2_deltaX = get_V_t2,
                kn_2_deltaFi = get_omega_t2;

        double
                get_fi_t3 = get_fi() + 0.5*get_step()*kn_2_deltaFi,
                get_V_t3 = get_V() + 0.5*get_step() * kn_2_deltaV,
                get_omega_t3 = get_omega() + 0.5*get_step() * kn_2_deltaOmega,
                getDeltax_t3 = getDeltax() + 0.5*get_step() * kn_2_deltaX,

                kn_3_delt = a11 * a22 - Math.pow(a12,2) * Math.pow(Math.cos(get_fi_t3),2),
                kn_3_Q1 = - ((get_k3() + get_k4()) * get_V_t3 + C1 * getDeltax_t3) - h1*Math.sin(get_p() * tn_h2),
                kn_3_Q2  = -get_k5() * get_omega_t3 - C2 * Math.sin(get_fi_t3),
                kn_3_deltaV = (1/kn_3_delt) * (a22 * kn_3_Q1 + a12 * a22 * Math.sin(get_fi_t3) * Math.pow(get_omega_t3,2) - kn_3_Q2*a12*Math.cos(get_fi_t3)),
                kn_3_deltaOmega = (1/kn_3_delt) * (a11 * kn_3_Q2 - a12 * kn_3_Q1 * Math.cos(get_fi_t3) - Math.pow(a12,2)*Math.pow(get_omega_t3,2)*Math.sin(get_fi_t3)*Math.cos(get_fi_t3) ),
                kn_3_deltaX = get_V_t3,
                kn_3_deltaFi = get_omega_t3;

        double tn_h4 = getT() + get_step(),
                get_fi_t4 = get_fi() + 0.5*get_step()*kn_3_deltaFi,
                get_V_t4 = get_V() + 0.5*get_step() * kn_3_deltaV,
                get_omega_t4 = get_omega() + 0.5*get_step() * kn_3_deltaOmega,
                getDeltax_t4 = getDeltax() + 0.5*get_step() * kn_3_deltaX,

                kn_4_delt = a11 * a22 - Math.pow(a12,2) * Math.pow(Math.cos(get_fi_t4),2),
                kn_4_Q1 = - ((get_k3() + get_k4()) * get_V_t4 + C1 * getDeltax_t4) - h1*Math.sin(get_p() * tn_h4),
                kn_4_Q2  = -get_k5() * get_omega_t4 - C2 * Math.sin(get_fi_t4),
                kn_4_deltaV = (1/kn_4_delt) * (a22 * kn_4_Q1 + a12 * a22 * Math.sin(get_fi_t4) * Math.pow(get_omega_t4,2) - kn_4_Q2*a12*Math.cos(get_fi_t4)),
                kn_4_deltaOmega = (1/kn_4_delt) * (a11 * kn_4_Q2 - a12 * kn_4_Q1 * Math.cos(get_fi_t4) - Math.pow(a12,2)*Math.pow(get_omega_t4,2)*Math.sin(get_fi_t4)*Math.cos(get_fi_t4) ),
                kn_4_deltaX = get_V_t4,
                kn_4_deltaFi = get_omega_t4;

        setT(tn_h4);
        set_V(get_V() + get_step()*(kn_1_deltaV + 2 * kn_2_deltaV + 2 * kn_3_deltaV + kn_4_deltaV)/6);
        set_omega(get_omega() + get_step()*(kn_1_deltaOmega + 2 * kn_2_deltaOmega + 2 * kn_3_deltaOmega + kn_4_deltaOmega)/6);
        setDeltax(getDeltax() + get_step()*(kn_1_deltaX + 2 * kn_2_deltaX + 2 * kn_3_deltaX + kn_4_deltaX)/6);
        set_fi(get_fi() + get_step()*(kn_1_deltaFi + 2 * kn_2_deltaFi + 2 * kn_3_deltaFi + kn_4_deltaFi)/6);

    }

    public void RungeKutte4_1(){
        BigDecimal grav = bdOf("9.8");
        BigDecimal a22 = (bdOf(get_m21())
                .multiply(
                        bdOf(get_l()).pow(2).divide(bdOf(3))
                )
        ).add(
                bdOf(get_m22())
                        .multiply( bdOf(get_i2()).pow(2))
                        .add(bdOf(get_r()).pow(2))
        );

/*
        double a22 = get_m21() * Math.pow(get_l(),2)/3 + get_m22() * (Math.pow(get_i2(),2) + Math.pow(get_r(),2));

                C2 = get_m21() * grav * get_l()/2 + get_m22() * grav * get_r(),

                kn_1_Q2  = -get_k5() * get_omega() - C2 * Math.sin(get_fi()),
                kn_1_deltaOmega = kn_1_Q2/a22,
                kn_1_deltaFi = get_omega();

*//*
        double tn_h2 = getT() + 0.5*get_step(),
                get_fi_t2 = get_fi() + 0.5*get_step()*kn_1_deltaFi,
                get_omega_t2 = get_omega() + 0.5*get_step() * kn_1_deltaOmega,

                kn_2_Q2  = -get_k5() * get_omega_t2 - C2 * Math.sin(get_fi_t2),
                kn_2_deltaOmega = kn_2_Q2/a22,
                kn_2_deltaFi = get_omega_t2;

        double
                get_fi_t3 = get_fi() + 0.5*get_step()*kn_2_deltaFi,
                get_omega_t3 = get_omega() + 0.5*get_step() * kn_2_deltaOmega,

                kn_3_Q2  = -get_k5() * get_omega_t3 - C2 * Math.sin(get_fi_t3),
                kn_3_deltaOmega = kn_3_Q2/a22,
                kn_3_deltaFi = get_omega_t3;

        double tn_h4 = getT() + get_step(),
                get_fi_t4 = get_fi() + 0.5*get_step()*kn_3_deltaFi,
                get_omega_t4 = get_omega() + 0.5*get_step() * kn_3_deltaOmega,

                kn_4_Q2  = -get_k5() * get_omega_t4 - C2 * Math.sin(get_fi_t4),
                kn_4_deltaOmega = kn_4_Q2/a22,
                kn_4_deltaFi = get_omega_t4;

        setT(tn_h4);
        set_omega(get_omega() + get_step()*(kn_1_deltaOmega + 2 * kn_2_deltaOmega + 2 * kn_3_deltaOmega + kn_4_deltaOmega)/6);
        set_fi(get_fi() + get_step()*(kn_1_deltaFi + 2 * kn_2_deltaFi + 2 * kn_3_deltaFi + kn_4_deltaFi)/6);
*/
    }

    private BigDecimal bdOf(double val) {
        return bdOf(String.valueOf(val));
    }

    private BigDecimal bdOf(String val) {
        return new BigDecimal(val).setScale(8, BigDecimal.ROUND_HALF_UP);
    }

    public void RungeKutte4_2(){
        double grav = 9.8;
        double
                a11 = get_m12() + get_m21() + get_m22() + 0.5*grav* get_m11(),
                C1 = get_k1() + get_k2(),
                h1 = get_k2() * get_r3(),
                C2 = get_m21() * grav * get_l()/2 + get_m22() * grav * get_r(),

                kn_1_Q1 = - ((get_k3() + get_k4()) * get_V() + C1 * getDeltax()) - h1*Math.sin(get_p() * getT()),
                kn_1_deltaV = kn_1_Q1/a11,
                kn_1_deltaX = get_V();


        double tn_h2 = getT() + 0.5*get_step(),
                get_V_t2 = get_V() + 0.5*get_step() * kn_1_deltaV,
                getDeltax_t2 = getDeltax() + 0.5*get_step() * kn_1_deltaX,

                kn_2_Q1 = - ((get_k3() + get_k4()) * get_V_t2 + C1 * getDeltax_t2) - h1*Math.sin(get_p() * tn_h2),
                kn_2_deltaV = kn_2_Q1/a11,
                kn_2_deltaX = get_V_t2;

        double
                get_V_t3 = get_V() + 0.5*get_step() * kn_2_deltaV,
                getDeltax_t3 = getDeltax() + 0.5*get_step() * kn_2_deltaX,

                kn_3_Q1 = - ((get_k3() + get_k4()) * get_V_t3 + C1 * getDeltax_t3) - h1*Math.sin(get_p() * tn_h2),
                kn_3_deltaV = kn_3_Q1/a11,
                kn_3_deltaX = get_V_t3;

        double tn_h4 = getT() + get_step(),
                get_V_t4 = get_V() + 0.5*get_step() * kn_3_deltaV,
                getDeltax_t4 = getDeltax() + 0.5*get_step() * kn_3_deltaX,

                kn_4_Q1 = - ((get_k3() + get_k4()) * get_V_t4 + C1 * getDeltax_t4) - h1*Math.sin(get_p() * tn_h4),
                kn_4_deltaV = kn_4_Q1/a11,
                kn_4_deltaX = get_V_t4;

        setT(tn_h4);
        set_V(get_V() + get_step()*(kn_1_deltaV + 2 * kn_2_deltaV + 2 * kn_3_deltaV + kn_4_deltaV)/6);
        setDeltax(getDeltax() + get_step()*(kn_1_deltaX + 2 * kn_2_deltaX + 2 * kn_3_deltaX + kn_4_deltaX)/6);

    }
}


