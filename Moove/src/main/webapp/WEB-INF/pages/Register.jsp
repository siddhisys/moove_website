<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Moove Register</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/Register.css">
</head>
<body>
	<div class="left">
		<img src="${pageContext.request.contextPath}/resources/images/RegisterPage/RegisterGIF.gif" class="gif"/>
	</div>

	<div class="right">
  <div class="form-container">
    <h1>CREATE ACCOUNT</h1>

    <div class="social-buttons">
      <button class="social-btn google-btn">
        <img src="${pageContext.request.contextPath}/resources/images/RegisterPage/googleicon.png" alt="Google" class="social-icon">
        Sign up with Google
      </button>

      <button class="social-btn facebook-btn">
        <img src="${pageContext.request.contextPath}/resources/images/RegisterPage/facebookicon.png" alt="Facebook" class="social-icon">
        Sign up with FaceBook
      </button>
    </div>

    <div class="or-divider">--OR--</div>

    <div class="form-section">
  <form action="${pageContext.request.contextPath}/Register" method="POST">
    <div class="form-group">
      <input type="text" id="username" name="username" placeholder="UserName" required>
    </div>

    <div class="form-group">
      <input type="password" id="password" name="password" placeholder="Password" required>
    </div>

    <div class="form-group">
      <input type="email" id="email" name="email" placeholder="Email" required>
    </div>

    <div class="form-group">
      <input type="text" id="address" name="address" placeholder="Address" required>
    </div>

    <div class="form-group dropdown-row">
      <select id="gender" name="gender" required>
        <option value="" disabled selected>Gender</option>
        <option value="Male">Male</option>
        <option value="Female">Female</option>
        <option value="Other">Other</option>
      </select>

      <select id="role" name="role" required>
        <option value="" disabled selected>Role</option>
        <option value="Instructor">Instructor</option>
        <option value="Student">Student</option>
        <option value="Parent">Parent</option>
      </select>
    </div>

    <button type="submit" class="create-account-btn">CREATE ACCOUNT</button>

    <div class="login-link">
      Already have an account? <a href="${pageContext.request.contextPath}/Login">Log in</a>
    </div>
  </form>
</div>

  </div>
  </div>
  
  <a href="${pageContext.request.contextPath}/landing" class="back-btn">&lt;</a>
  
</body>
</html>