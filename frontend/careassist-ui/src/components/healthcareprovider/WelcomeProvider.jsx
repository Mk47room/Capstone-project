function WelcomeProvider(){
    return(
        <div>
            <h5>Welcome {localStorage.getItem('username')} Hospital</h5>
        </div>
    )
}

export default WelcomeProvider