import SubmitButton from "./SubmitButton";
import Button from "./Button";

const ActivateForm = ({onSubmit, newToken}) => {
    return <form id="activated" onSubmit={onSubmit}>
        <h2>Activated</h2>
        <label htmlFor="token">Token:</label>
        <input type="text" id="token" name="token" required/>
        <SubmitButton>Activate</SubmitButton>
        <Button onClick={newToken} type="button" id="new-refresh-token">New token</Button>
    </form>
}

export default ActivateForm;
