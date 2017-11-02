package wstaw.sie.model.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wstaw.sie.model.entity.User;

public class AdminPasswordChangeForm {

	private Result result = new Result();

	public class Result {
		private Map<User, String> positiveResetedPassword = new HashMap<User, String>();
		private List<User> negativeResetedPassword = new ArrayList<User>();

		public void addPositiveResult(User user, String newPassword) {
			this.positiveResetedPassword.put(user, newPassword);
		}

		public void addNegativeResult(User id) {
			this.negativeResetedPassword.add(id);
		}

		public  Map<User, String>  getPositiveResetedPassword() {
			return positiveResetedPassword;
		}

		public void setPositiveResetedPassword( Map<User, String>  positiveResetedPassword) {
			this.positiveResetedPassword = positiveResetedPassword;
		}

		public List<User> getNegativeResetedPassword() {
			return negativeResetedPassword;
		}

		public void setNegativeResetedPassword( List<User> negativeResetedPassword) {
			this.negativeResetedPassword = negativeResetedPassword;
		}

		
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

}
