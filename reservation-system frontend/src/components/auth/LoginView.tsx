import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { useAppDispatch } from "../../store/hooks";
import { loginApi } from "../../api/login";
import { setCredentials } from "../../store/features/authSlice";
import { AxiosError, AxiosResponse } from "axios";
import { toast } from "react-toastify";
import { getUserById } from "@/api/users";

const loginSchema = z.object({
  username: z
    .string()
    .min(1, "Username must be at least 1 characters")
    .max(50, "Username cannot exceed 50 characters"),
  password: z
    .string()
    .min(1, "Password must be at least 1 characters")
    .max(50, "Password cannot exceed 50 characters"),
});

type LoginFormData = z.infer<typeof loginSchema>;

export function LoginForm() {
  const dispatch = useAppDispatch();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (loginData: {
    username: string;
    password: string;
  }) => {
    try {
      const response = await loginApi(loginData);

      if ((response as AxiosResponse)?.status === 200) {
        dispatch(
          setCredentials({
            token: (response as AxiosResponse).data?.token,
            username: loginData.username,
            role: (response as AxiosResponse).data?.role,
            id: (response as AxiosResponse).data?.id,
          })
        );
        
        if ((response as AxiosResponse).data?.role == "MANAGER") {
          const manager = await getUserById((response as AxiosResponse).data?.id);
          localStorage.setItem("restaurant", manager.restaurantName);
        }
        toast.success("Login successful!");
      } else {
        toast.error(
          ((response as AxiosError)?.response?.data as string) ||
            "Login failed!"
        );
      }
    } catch (error) {
      console.log(error);
      toast.error("An unexpected error occurred!");
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="space-y-4 w-1/3 text-cyan-100"
    >
      <div>
        <label htmlFor="username" className="block text-sm font-medium">
          Username
        </label>
        <input
          {...register("username")}
          type="text"
          id="username"
          className="mt-1 px-3 py-2 block w-full rounded-md border-gray-300 shadow-sm"
        />
        {errors.username && (
          <p className="mt-1 text-sm text-red-600">{errors.username.message}</p>
        )}
      </div>

      <div>
        <label htmlFor="password" className="block text-sm font-medium">
          Password
        </label>
        <input
          {...register("password")}
          type="password"
          id="password"
          className="mt-1 px-3 py-2 block w-full rounded-md border-gray-300 shadow-sm"
        />
        {errors.password && (
          <p className="mt-1 text-sm text-red-600">{errors.password.message}</p>
        )}
      </div>

      <button
        type="submit"
        disabled={isSubmitting}
        className="w-full rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700 disabled:opacity-50"
      >
        {isSubmitting ? "Logging in..." : "Login"}
      </button>
    </form>
  );
}
