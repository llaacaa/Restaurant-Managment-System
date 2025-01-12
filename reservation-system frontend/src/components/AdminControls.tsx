import { blockUser, getUsers } from "@/api/users";
import { useAppSelector } from "@/store/hooks";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";

interface User {
  id: number;
  username: string;
  password: string;
  email: string;
  dateOfBirth: string;
  name: string;
  surname: string;
  role: "CLIENT" | "ADMIN" | "MANAGER";
  numberOfReservations: number;
  active: boolean;
  blocked: boolean;
}

function AdminControls() {
  const [users, setUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const auth = useAppSelector((state) => state.auth);

  const fetchAllUsers = async () => {
    try {
      setIsLoading(true);
      const response = await getUsers();
      if (Array.isArray(response)) {
        setUsers(response);
      } else {
        console.error("Invalid data format:", response.data);
        toast.error("Invalid user data received.");
      }
    } catch (error) {
      console.error("Error while fetching users:", error);
      toast.error("Error while fetching users.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchAllUsers();
  }, []);

  const handleToggleBlock = async (id: number, isBlock:boolean) => {
    const blocked = isBlock ? "blocked" : "unblocked";
    try {
      await blockUser(auth.token as string, id);
      toast.success(`User with ID ${id} ${blocked} successfully.`);
      await fetchAllUsers(); // Refetch users after the block/unblock action
    } catch (error) {
      console.error("Error toggling user block:", error);
      toast.error("Failed to toggle user block.");
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-6 bg-gray-100 shadow-md rounded-lg">
      <h1 className="text-2xl text-cyan-950 font-bold text-center mb-4">
        Admin Controls
      </h1>

      {isLoading ? (
        <p className="text-center text-blue-500">Loading...</p>
      ) : Array.isArray(users) && users.length > 0 ? (
        <ul className="divide-y divide-gray-300">
          {users.map((user) => (
            <li
              key={user.id}
              className="py-4 px-2 bg-white hover:bg-gray-50 rounded-md shadow-sm mb-3"
            >
              <div className="flex justify-between items-center">
                <div>
                  <p className="font-semibold text-lg">{user.username}</p>
                  <p className="text-sm text-gray-600">Role: {user.role}</p>
                </div>
                {user.role !== "ADMIN" && (
                  <div className="flex space-x-2">
                    {user.blocked ? (
                      <button
                        onClick={() => handleToggleBlock(user.id, false)}
                        className="bg-green-600 text-white py-1 px-4 rounded-md hover:bg-green-700"
                      >
                        Unblock
                      </button>
                    ) : (
                      <button
                        onClick={() => handleToggleBlock(user.id, true)}
                        className="bg-red-600 text-white py-1 px-4 rounded-md hover:bg-red-700"
                      >
                        Block
                      </button>
                    )}
                  </div>
                )}
              </div>
              <div className="mt-2 text-sm text-gray-700">
                <p>
                  <span className="font-medium">Name:</span> {user.name}{" "}
                  {user.surname}
                </p>
                <p>
                  <span className="font-medium">Email:</span> {user.email}
                </p>
                <p>
                  <span className="font-medium">Date of Birth:</span>{" "}
                  {user.dateOfBirth}
                </p>
                <p>
                  <span className="font-medium">Reservations:</span>{" "}
                  {user.numberOfReservations}
                </p>
                <p>
                  <span className="font-medium">Active:</span>{" "}
                  {user.active ? "Yes" : "No"}
                </p>
                <p>
                  <span className="font-medium">Blocked:</span>{" "}
                  {user.blocked ? "Yes" : "No"}
                </p>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p className="text-center text-gray-500">No users available</p>
      )}
    </div>
  );
}

export default AdminControls;
