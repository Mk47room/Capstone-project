import InsurerClaimChart from "./InsurerClaimChart"
import InsurerStat from "./InsurerStat"

function InsurerHome() {

    return(
        <div>
            <div>
                <h5>Welcome back, {localStorage.getItem('username')}</h5>
            </div>
            <div>
                <InsurerStat />
            </div>
            <div>
                <InsurerClaimChart />
            </div>
        </div>
    )
}

export default InsurerHome