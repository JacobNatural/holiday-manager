import GetUserData from "./GetUser";
import Button from "../components/Button";
import {useNavigate} from "react-router-dom";


const ActualAdminUser = () => {
    const navigate = useNavigate();

    const handleSetting = () =>{
        navigate("/settings ")
    }

    const handleHolidayManager = () =>{
        navigate("/holiday-manager")
    }

    const handleUserManager = () => {
        navigate("/user-manager")
    }

    return(<div id={"actual-admin-user"} className={'container'}>
        <h2 id={'actual-user-h2'}>Actual Worker</h2>
        <Button id={'settings'} onClick={handleSetting}>Settings</Button>
        <GetUserData/>
        <Button id={'user-manager'} onClick={handleUserManager}>user manager</Button>
        <Button id={'holiday-manager'} onClick={handleHolidayManager}>holiday manager</Button>
    </div>);
}

export default ActualAdminUser;