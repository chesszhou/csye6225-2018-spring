package CSYE6225.Spring2018;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static CSYE6225.Spring2018.WebSecurityConfig.SESSION_KEY;

@Controller
public class MainController {

	@GetMapping("/")
	public String index(@SessionAttribute(SESSION_KEY) String account, Model model) {
		model.addAttribute("name", account);
		model.addAttribute("time", new Date());
		return "index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("time", new Date());
		return "login";
	}

	@PostMapping("/loginPost")
	public @ResponseBody Map<String, Object> loginPost(String account, String password, HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		if (!"123456".equals(password)) {
			map.put("success", false);
			map.put("message", "Invalid Information!");
			return map;
		}


		session.setAttribute(SESSION_KEY, account);

		map.put("success", true);
		map.put("message", "Success");
		map.put("time", new Date());
		return map;
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.removeAttribute(SESSION_KEY);
		return "redirect:/login";
	}

}
