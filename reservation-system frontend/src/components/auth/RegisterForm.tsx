import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { registerClient, registerManager } from "@/api/login";
import { AxiosError, AxiosResponse } from "axios";
import { toast } from "react-toastify";

// Base schema for shared fields
const baseUserSchema = z.object({
  username: z
    .string()
    .min(1, "Username must be at least 1 characters")
    .max(50, "Username cannot exceed 50 characters"),
  email: z.string().email("Invalid email address").min(5, "Email is required"),
  password: z
    .string()
    .min(3, "Password must be at least 3 characters"),
  dateOfBirth: z.string().refine((date) => new Date(date) <= new Date(), {
    message: "Date of birth cannot be in the future",
  }),
  name: z
    .string()
    .min(2, "Name must be at least 2 characters")
    .max(50, "Name cannot exceed 50 characters"),
  surname: z
    .string()
    .min(2, "Surname must be at least 2 characters")
    .max(50, "Surname cannot exceed 50 characters"),
});

// Client schema (just uses base schema)
const clientSchema = baseUserSchema;

// Manager schema (extends base schema)
const managerSchema = baseUserSchema.extend({
  restaurantName: z
    .string()
    .min(2, "Restaurant name must be at least 2 characters")
    .max(100, "Restaurant name cannot exceed 100 characters"),
  dateOfEmployment: z.string().refine((date) => new Date(date) <= new Date(), {
    message: "Employment date cannot be in the future",
  }),
});

export type ClientFormData = z.infer<typeof clientSchema>;
export type ManagerFormData = z.infer<typeof managerSchema>;

const InputField = ({ label, type = "text", error, register, id }: any) => (
  <div className="mb-4">
    <label htmlFor={id} className="block text-sm font-medium mb-1">
      {label}
    </label>
    <input
      {...register(id)}
      type={type}
      id={id}
      className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
    />
    {error && <p className="mt-1 text-sm text-red-600">{error.message}</p>}
  </div>
);

export function RegisterForms({
  setIsLogin,
}: {
  setIsLogin: (arg0: boolean) => void;
}) {
  const [isManager, setIsManager] = useState(false);

  return (
    <div className="max-w-md mx-auto mt-8 ">
      <div className="mb-4 flex justify-center space-x-4">
        <button
          onClick={() => setIsManager(false)}
          className={`px-4 py-2 rounded ${
            !isManager ? "bg-blue-600 text-white" : ""
          }`}
        >
          Client Registration
        </button>
        <button
          onClick={() => setIsManager(true)}
          className={`px-4 py-2 rounded ${
            isManager ? "bg-blue-600 text-white" : ""
          }`}
        >
          Manager Registration
        </button>
      </div>

      {isManager ? (
        <ManagerForm setIsLogin={setIsLogin} />
      ) : (
        <ClientForm setIsLogin={setIsLogin} />
      )}
    </div>
  );
}

function ClientForm({ setIsLogin }: { setIsLogin: (arg0: boolean) => void }) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<ClientFormData>({
    resolver: zodResolver(clientSchema),
  });

  const onSubmit = async (data: ClientFormData) => {
    try {
      const response: AxiosResponse | unknown = await registerClient(data);
      if (
        (response as AxiosResponse)?.status == 200 ||
        (response as AxiosResponse)?.status == 201
      ) {
        toast.success("Registration successful!");
        setIsLogin(true);
      } else {
        toast.error(
          ((response as AxiosError)?.response?.data as string) ||
            "Registration failed!"
        );
      }
    } catch (error) {
      console.error("Registration failed:", error);
      toast.error("Registration failed!");
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <h2 className="text-2xl font-bold text-center mb-6">
        Client Registration
      </h2>

      <InputField
        label="Username"
        register={register}
        id="username"
        error={errors.username}
      />
      <InputField
        label="Email"
        type="email"
        register={register}
        id="email"
        error={errors.email}
      />
      <InputField
        label="Password"
        type="password"
        register={register}
        id="password"
        error={errors.password}
      />
      <InputField
        label="Name"
        register={register}
        id="name"
        error={errors.name}
      />
      <InputField
        label="Surname"
        register={register}
        id="surname"
        error={errors.surname}
      />
      <InputField
        label="Date of Birth"
        type="date"
        register={register}
        id="dateOfBirth"
        error={errors.dateOfBirth}
      />

      <button
        type="submit"
        disabled={isSubmitting}
        className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 disabled:opacity-50"
      >
        {isSubmitting ? "Registering..." : "Register as Client"}
      </button>
    </form>
  );
}

function ManagerForm({ setIsLogin }: { setIsLogin: (arg0: boolean) => void }) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<ManagerFormData>({
    resolver: zodResolver(managerSchema),
  });

  const onSubmit = async (data: ManagerFormData) => {
    try {
      const response: AxiosResponse | unknown = await registerManager(data);

      if (
        (response as AxiosResponse)?.status == 200 ||
        (response as AxiosResponse)?.status == 201
      ) {
        toast.success("Registration successful!");
        setIsLogin(true);
      } else {
        toast.error(
          ((response as AxiosError)?.response?.data as string) ||
            "Registration failed!"
        );
      }
    } catch (error) {
      console.error("Registration failed:", error);
      toast.error("Registration failed!");
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 mb-5">
      <h2 className="text-2xl font-bold text-center mb-6">
        Manager Registration
      </h2>

      <InputField
        label="Username"
        register={register}
        id="username"
        error={errors.username}
      />
      <InputField
        label="Email"
        type="email"
        register={register}
        id="email"
        error={errors.email}
      />
      <InputField
        label="Password"
        type="password"
        register={register}
        id="password"
        error={errors.password}
      />
      <InputField
        label="Name"
        register={register}
        id="name"
        error={errors.name}
      />
      <InputField
        label="Surname"
        register={register}
        id="surname"
        error={errors.surname}
      />
      <InputField
        label="Date of Birth"
        type="date"
        register={register}
        id="dateOfBirth"
        error={errors.dateOfBirth}
      />
      <InputField
        label="Restaurant Name"
        register={register}
        id="restaurantName"
        error={errors.restaurantName}
      />
      <InputField
        label="Date of Employment"
        type="date"
        register={register}
        id="dateOfEmployment"
        error={errors.dateOfEmployment}
      />

      <button
        type="submit"
        disabled={isSubmitting}
        className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 disabled:opacity-50"
      >
        {isSubmitting ? "Registering..." : "Register as Manager"}
      </button>
    </form>
  );
}
