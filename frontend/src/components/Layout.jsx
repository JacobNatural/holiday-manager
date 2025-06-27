import Logout from "../pages/Logout";
import {Outlet} from "react-router-dom";
import {useContext, useState} from "react";
import {AuthContext} from "../context/CreateContext";

const Layout = () => {
    const {auth} = useContext(AuthContext);
        return (<div>
        <header>
            {auth && <Logout/>}
        </header>
        <main>
            <Outlet /> {}
        </main>
    </div>)
}

export default Layout;