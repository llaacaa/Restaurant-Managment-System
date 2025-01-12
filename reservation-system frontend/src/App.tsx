import { LoginComponent } from "./components/auth/LoginComponent";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
} from "@/components/ui/navigation-menu";
import { useAppDispatch, useAppSelector } from "./store/hooks";
import { logout } from "./store/features/authSlice";
import { useState } from "react";
import UpdateInformation from "./components/UpdateInformation";
import AdminControls from "./components/AdminControls";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Restaurants from "./components/Restaurants";
import TimeSlotsList from "./components/TimeSlotsList";
import ReservationList from "./components/ReservationList";
import LoyaltyProgram from "./components/LoyaltyProgram";

function App() {
  const auth = useAppSelector((state) => state.auth);

  const dispatch = useAppDispatch();

  const handleLogout = () => {
    dispatch(logout());
  };

  const [page, setPage] = useState(0);

  return (
    <>
      <nav className="shadow-sm absolute top-0 w-screen">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              {auth.isAuthenticated && <DropdownMenu>
                <DropdownMenuTrigger>Restaurant</DropdownMenuTrigger>
                <DropdownMenuContent>
                  <DropdownMenuItem>
                    <section onClick={() => setPage(0)} className="hover:text-slate-400 cursor-pointer">Restaurants</section>
                  </DropdownMenuItem>
                  <DropdownMenuItem>
                    <section onClick={() => setPage(3)} className="hover:text-slate-400 cursor-pointer">Time Slots</section>
                  </DropdownMenuItem>
                  <DropdownMenuItem>
                    <section onClick={() => setPage(4)} className="hover:text-slate-400 cursor-pointer">Reservations</section>
                  </DropdownMenuItem>
                  {auth.role == "MANAGER" && <DropdownMenuItem>
                    <section onClick={() => setPage(5)} className="hover:text-slate-400 cursor-pointer">Loyalty Program</section>
                  </DropdownMenuItem>}
                </DropdownMenuContent>
              </DropdownMenu>}
            </div>

            <div className="flex items-center space-x-4">
              {auth.isAuthenticated && (
                <NavigationMenu>
                  <NavigationMenuList>
                    <NavigationMenuItem>
                      <NavigationMenuTrigger className="flex items-center space-x-2">
                        <div className="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center">
                          <span className="text-white font-medium">
                            {auth.username?.charAt(0).toUpperCase()}
                          </span>
                        </div>
                        <div className="text-sm">
                          <p className=" font-medium">
                            Welcome, {auth.username}!
                          </p>
                          <p className="text-gray-500 text-xs">Signed in</p>
                        </div>
                      </NavigationMenuTrigger>
                      <NavigationMenuContent>
                        <NavigationMenuLink onClick={() => setPage(1)}>
                          <div className="m-2 w-max cursor-pointer hover:text-blue-600">
                            Update information
                          </div>
                        </NavigationMenuLink>
                        {auth.role === "ADMIN" && (
                          <NavigationMenuLink onClick={() => setPage(2)}>
                            <div className="m-2 cursor-pointer hover:text-blue-600">
                              Admin Controls
                            </div>
                          </NavigationMenuLink>
                        )}
                        <NavigationMenuLink
                          onClick={() => {
                            handleLogout();
                            setPage(0);
                          }}
                        >
                          <div className="m-2 cursor-pointer hover:text-blue-600">
                            Logout
                          </div>
                        </NavigationMenuLink>
                      </NavigationMenuContent>
                    </NavigationMenuItem>
                  </NavigationMenuList>
                </NavigationMenu>
              )}
            </div>
          </div>
        </div>
        <ToastContainer />
      </nav>

      {page == 0 && !auth.isAuthenticated && <LoginComponent />}
      {page == 0 && auth.isAuthenticated && <Restaurants />}
      {page == 1 && <UpdateInformation role={auth.role} token={auth.token} />}
      {page == 2 && <AdminControls />}
      {page == 3 && <TimeSlotsList />}
      {page == 4 && <ReservationList/>}
      {page == 5 && <LoyaltyProgram/>}
    </>
  );
}

export default App;
