import Home from './pages/Home';
import './App.css';
import React, {useEffect, useState} from "react";
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Login from "./pages/Login";
import ActualWorkerUser from "./pages/ActualWorkerUser";
import Holidays from "./pages/Holidays";
import Settings from "./pages/Settings";
import ChangePassword from "./pages/ChangePassword";
import Register from "./pages/Register";
import Activate from "./pages/Activate";
import ChangeEmail from "./pages/ChangeEmail";
import ActualAdminUser from "./pages/ActualAdminUser";
import HolidayManager from "./pages/HolidayManager";
import UserManager from "./pages/UserManager";
import ProtectedRoute from "./routes/ProtectedRoute";
import ForgotPassword from "./pages/ForgotPassword";
import NewPassword from "./pages/NewPassword";
import Layout from "./components/Layout";
import {AuthContext} from "./context/CreateContext";


function App() {

    const [auth, setAuth] = useState(false);

    useEffect(() => {
        const auth = JSON.parse(localStorage.getItem('auth'));
        setAuth(auth);
    },[])
    return (
        <Router>
            <AuthContext.Provider value={{auth, setAuth}}>
            <Routes>
                <Route element={<Layout/>}>
                    <Route path={"/"} element={<Home/>}/>
                    <Route path={"/login"} element={<Login/>}/>
                    <Route path={"/register"} element={<Register/>}/>
                    <Route path={"/activate"} element={<Activate/>}/>
                    <Route path={"/forgot-password"} element={<ForgotPassword/>}/>
                    <Route path={"new-password"} element={<NewPassword/>}/>
                    <Route path={"/worker"} element={<ProtectedRoute><ActualWorkerUser/></ProtectedRoute>}/>
                    <Route path={"/holidays"} element={<ProtectedRoute><Holidays/></ProtectedRoute>}/>
                    <Route path={"/settings"} element={<ProtectedRoute><Settings/></ProtectedRoute>}/>
                    <Route path={"/change-password"} element={<ProtectedRoute><ChangePassword/></ProtectedRoute>}/>
                    <Route path={"/change-email"} element={<ProtectedRoute><ChangeEmail/></ProtectedRoute>}/>
                    <Route path={"/admin"} element={<ProtectedRoute><ActualAdminUser/></ProtectedRoute>}/>
                    <Route path={"holiday-manager"} element={<ProtectedRoute><HolidayManager/></ProtectedRoute>}/>
                    <Route path={"/user-manager"} element={<ProtectedRoute><UserManager/></ProtectedRoute>}/>
                </Route>
            </Routes>
            </AuthContext.Provider>
        </Router>);
}

export default App;
