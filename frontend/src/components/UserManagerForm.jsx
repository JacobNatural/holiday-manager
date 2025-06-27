import SubmitButton from "./SubmitButton";

const UserManagerForm = ({onSubmit, name,nameOnChange, surname, surnameOnChange, email, emailOnChange, username, usernameOnChange}) =>
     <form id="user-searching-form" onSubmit={onSubmit} >
        <label htmlFor="name" id="labelNameUserManager">Name:</label>
        <input type="text" id="nameUserManager" name="name" value={name} onChange={nameOnChange}/>

        <label htmlFor="surname" id="labelSurnameUserManager">Surname:</label>
        <input type="text" id="surnameUserManager" name="surname" value={surname} onChange={surnameOnChange}/>

        <label htmlFor="email" id="labelEmailUserManager">Email:</label>
        <input type="email" id="emailUserManager" name="email" value={email} onChange={emailOnChange}/>

        <label htmlFor="username" id="labelUsernameUserManager">Username:</label>
        <input type="text" id="usernameUserManager" name="username" value={username} onChange={usernameOnChange}    />

         <SubmitButton id="submitUserManager">search</SubmitButton>
    </form>

export default UserManagerForm;
