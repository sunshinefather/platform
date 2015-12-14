package com.platform.common.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.platform.common.utils.StringUtils;

/**
 * 验证码servlet(生成与校验)
 * @ClassName:  ValidateCodeServlet   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2015年12月11日 上午11:34:43
 */
public class ValidateCodeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public static final String VALIDATE_CODE = "validateCode";
	
	private int w = 70;
	private int h = 26;
	
	public ValidateCodeServlet() {
		super();
	}
	
	public void destroy() {
		super.destroy(); 
	}
	
	public static boolean validate(HttpServletRequest request, String validateCode){
		String code = (String)request.getSession().getAttribute(VALIDATE_CODE);
		return validateCode.toUpperCase().equals(code); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String validateCode = request.getParameter(VALIDATE_CODE);
		if (StringUtils.isNotBlank(validateCode)){
			response.getOutputStream().print(validate(request, validateCode)?"true":"false");
		}else{
			this.doPost(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		createImage(request,response);
	}
	
	private void createImage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		if (StringUtils.isNumeric(width) && StringUtils.isNumeric(height)) {
			w = StringUtils.toInteger(width);
			h = StringUtils.toInteger(height);
		}
		
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		createBackground(g);
		String code = StringUtils.getRandomCode(3, 4,null);
		request.getSession().setAttribute(VALIDATE_CODE, code);
		drawStr(g,code);
		g.dispose();
		OutputStream out = response.getOutputStream();
		ImageIO.write(image, "JPEG", out);
		out.close();

	}
	
	private Color getRandColor(int fc,int bc) { 
		int f = fc;
		int b = bc;
		Random random=new Random();
        if(f>255) {
        	f=255; 
        }
        if(b>255) {
        	b=255; 
        }
        return new Color(f+random.nextInt(b-f),f+random.nextInt(b-f),f+random.nextInt(b-f)); 
	}
	
	private void createBackground(Graphics g) {
		g.setColor(getRandColor(220,250)); 
		g.fillRect(0, 0, w, h);
		for (int i = 0; i < 8; i++) {
			g.setColor(getRandColor(40,150));
			Random random = new Random();
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			int x1 = random.nextInt(w);
			int y1 = random.nextInt(h);
			g.drawLine(x, y, x1, y1);
		}
	}

	private void drawStr(Graphics g,String code) {
		String[] fontTypes = {"Arial","Arial Black","AvantGarde Bk BT","Calibri"}; 
		Random random = new Random();
		for (int i = 0; i < code.length(); i++) {
			String r=code.substring(i,i+1);
			g.setColor(new Color(50 + random.nextInt(100), 50 + random.nextInt(100), 50 + random.nextInt(100)));
			g.setFont(new Font(fontTypes[random.nextInt(fontTypes.length)],Font.BOLD,26)); 
			g.drawString(r, 15 * i + 5, 19 + random.nextInt(8));
		}
	}	
}