package com.atm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController{
	@Autowired 
	public UserService userService;
	
	public 	RedirectView redirectView=new RedirectView();
	
	@GetMapping("/ATM")
	public String home() {
		return "index";
	}
	
	@PostMapping("/Create")
	public String create(@ModelAttribute BankAccount user,RedirectAttributes redirectAttributes) {
		if(user.balance<=0 || user.accountNumber.length()<8 || user.accountNumber.length()>10) {
			if(user.accountNumber.charAt(0)=='-') {
				redirectAttributes.addFlashAttribute("errorMessage", "Account Number Cannot be Negative!");
				return "redirect:/ATM";	
			}
			redirectAttributes.addFlashAttribute("errorMessage", "Invalid Balance or Account Number Must be 8-10 digits!");
			return "redirect:/ATM"; 
		}
		userService.save(user);
		redirectAttributes.addFlashAttribute("successMessage", "Account Created Successfully!");
		return "redirect:/ATM"; 
	}
	
	@GetMapping("/VisitDashboard")
	public String dash() {
		return "dashboardId";
	}
	
	@GetMapping("/goToDashboard")
	public String GoToDashboard(@RequestParam("AccNo") String AccNo, Model model) {
	    try {
	        BankAccount ba = userService.getUserByAccountNumber(AccNo);
	        if (ba != null) {
	            String url = "/dashboard/" + ba.getId(); 
	            return "redirect:" + url;
	        } else {
	            model.addAttribute("errorMessage", "Account not found");
	        }
	    } catch (Exception e) {
	        model.addAttribute("errorMessage", "Something went wrong");
	    }
	    return "dashboardId"; 
	}


	@GetMapping("/delete/{id}")
	public RedirectView delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		try {
	    this.userService.deleteUser(id);
	    redirectAttributes.addFlashAttribute("successMessage", "Account successfully deleted!");
	    redirectView.setUrl("/ATM");
	    return redirectView;
	    }catch(Exception e) {
	    	e.printStackTrace();
	    	redirectAttributes.addFlashAttribute("errorMessage", "Something Went Wrong!");
	    }
		redirectView.setUrl("/ATM");
		return redirectView;
	}


	
	@GetMapping("/dashboard/{id}")
	public String dashboard(Model model, @PathVariable("id") int id) {
	    BankAccount user = userService.getUserById(id);
	    model.addAttribute("user", user);
	    return "dashboard";
	}
	
	@GetMapping("/deposit")
	public String showDepositForm() {
	    return "deposit";
	}


	@GetMapping("/instructions")
	public String instructions() {
		return "instructions";
	}
	
	@PostMapping("/deposit")
	public String deposit(@RequestParam("RecAccNo") String RecAccNo,@RequestParam("AccNo") String AccNo,@RequestParam("amount") double amount,RedirectAttributes redirectAttributes) {
        BankAccount receiveUser = userService.getUserByAccountNumber(RecAccNo);
        BankAccount sendUser = userService.getUserByAccountNumber(AccNo);
	    if (amount <= 0) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Amount must be greater than zero.");
	    } else {
	        if (receiveUser != null && sendUser != null && amount<=sendUser.getBalance()) {
	            if (userService.deposit(receiveUser, amount)) {
	                redirectAttributes.addFlashAttribute("successMessage", "Credited Successfully!");
	                userService.updateUser(receiveUser, receiveUser.id);
	                if(!receiveUser.equals(sendUser)) {
	                userService.withdraw(sendUser, amount);
	                userService.updateUser(sendUser, sendUser.id);
	                }
	            } else {
	                redirectAttributes.addFlashAttribute("errorMessage", "Transaction Denied! Something went wrong.");
	            }
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Transaction Denied! Insufficient balance or Account Not Found!");
	        }
	    }
	    return "redirect:/deposit";
	}

	@GetMapping("/withdraw/{id}")
	public String showWithdrawForm(Model model, @PathVariable ("id") int id) {
	    BankAccount user = userService.getUserById(id);
	    model.addAttribute("user", user);
	    return "withdraw";
	}

	@PostMapping("/withdraw/{id}")
	public String withdraw(
	    @RequestParam String AccNo,
	    @RequestParam double amount,
	    @PathVariable("id") int id,
	    RedirectAttributes redirectAttributes
	) {
	    if (amount <= 0) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Amount must be greater than zero.");
	    } else {
	        BankAccount user = userService.getUserById(id);
	        if (user != null && user.getAccountNumber().equals(AccNo) && amount < user.getBalance()) {
	            user.setBalance(user.getBalance() - amount);
	            userService.updateUser(user, id);
	            redirectAttributes.addFlashAttribute("successMessage", "Debited Successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Transaction Denied! Insufficient balance or Account Not Found!");
	        }
	    }
	    return "redirect:/withdraw/{id}";
	}
	
	@GetMapping("/Balance/{id}")
	public String balance(Model model,@PathVariable ("id") int id) {
		BankAccount user=this.userService.getUserById(id);
		model.addAttribute("user", user);
		return "balance";
	}
	
}