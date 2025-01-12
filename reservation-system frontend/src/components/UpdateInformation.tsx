import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-toastify";
import fetchData from "@/api/users";
import { useEffect, useState } from "react";
import { updateUser } from "@/api/update";
import { AxiosResponse } from "axios";

export function UpdateInformation({
  role,
  token,
}: {
  role: string | null;
  token: string | null;
}) {
  if (role == "MANAGER" && token != null) {
    return (
      <div className="w-1/3 justify-self-center">
        <ManagerForm token={token} />
      </div>
    );
  } else if ((role == "ADMIN" || role == "CLIENT") && token != null) {
    return (
      <div className="w-1/2 justify-self-center">
        <ClientForm token={token} />
      </div>
    );
  } else {
    return <p>Error</p>;
  }
}

export default UpdateInformation;

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

const clientSchema = baseUserSchema;

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

function ClientForm({ token }: { token: string }) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<ClientFormData>({
    resolver: zodResolver(clientSchema),
  });

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const data = await fetchData(token);
        setId(data.id);
        reset(data);
      } catch (error) {
        console.error("Error fetching user data:", error);
        toast.error("Failed to fetch user data");
      }
    };

    fetchUserData();
  }, [token, reset]);

  const onSubmit = async (data: ClientFormData) => {
    try {
      const response = await updateUser(token, data, id);
      if ((response as AxiosResponse)?.status === 200 || (response as AxiosResponse)?.status === 201) {
        toast.success("Client updated successfully!");
      } else {
        toast.error("Client update failed!");
      }
    } catch (error) {
      console.error("Client update failed:", error);
      toast.error("Client update failed!");
    }
  };

  const [id, setId] = useState(0);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
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
        {isSubmitting ? "Updating..." : "Update Client"}
      </button>
    </form>
  );
}

function ManagerForm({ token }: { token: string }) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<ManagerFormData>({
    resolver: zodResolver(managerSchema),
  });

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const data = await fetchData(token);
        reset(data);
        setId(data.id);
      } catch (error) {
        console.error("Error fetching user data:", error);
        toast.error("Failed to fetch user data");
      }
    };

    fetchUserData();
  }, [token, reset]);

  const onSubmit = async (data: ManagerFormData) => {
    try {
      const response = await updateUser(token, data, id);
      if ((response as AxiosResponse)?.status === 200 || (response as AxiosResponse)?.status === 201) {
        toast.success("Manager updated successfully!");
      } else {
        toast.error("Manager update failed!");
      }
    } catch (error) {
      console.error("Manager update failed:", error);
      toast.error("Manager update failed!");
    }
  };

  const [id, setId] = useState(0);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
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
        {isSubmitting ? "Updating..." : "Update Manager"}
      </button>
    </form>
  );
}
