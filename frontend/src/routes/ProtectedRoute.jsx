import {Navigate} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {AuthContext} from "../context/CreateContext";

const ProtectedRoute = ({children}) => {
    const {auth} = useContext(AuthContext);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if(auth) {
            setLoading(false);
        }
    },[auth])

    if(loading){
        return <div>Loading...</div>;
    }

    return auth ? children : <Navigate to='/login' replace/>
}

export default ProtectedRoute