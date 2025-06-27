import SubmitButton from "./SubmitButton";

const ForgotPasswordForm = ({onSubmit}) => {
    return <form id="email" className="container" onSubmit={onSubmit} >
        <label htmlFor="email">Email:</label>
        <input type="text" id="email" name="email" required/>
        <SubmitButton id={"get-token"}>New password</SubmitButton>
    </form>
}

export default ForgotPasswordForm;