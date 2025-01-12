import { useState } from "react";
import { LoginForm } from "./LoginView";
import { RegisterForms } from "./RegisterForm";
import { Button } from "@/components/ui/button";

export function LoginComponent() {

  const [isLogin, setIsLogin] = useState(true);

  return (
    <div className="w-screen min-h-screen flex flex-col items-center pt-10">
      {isLogin && (
        <div className="mb-8 text-center">
          <p className="text-gray-600 mb-2">Don't have an account?</p>
          <Button
            onClick={() => setIsLogin(false)}
            className="text-blue-600 hover:text-blue-800 font-medium"
          >
            Register here
          </Button>
        </div>
      )}

      {!isLogin && (
        <div className="mb-8 text-center">
          <p className="text-gray-600 mb-2">Already have an account?</p>
          <Button
            onClick={() => setIsLogin(true)}
            className="text-blue-600 hover:text-blue-800 font-medium"
          >
            Login here
          </Button>
        </div>
      )}

      {isLogin && <LoginForm />}
      {!isLogin && <RegisterForms setIsLogin={setIsLogin} />}
    </div>
  );
}
