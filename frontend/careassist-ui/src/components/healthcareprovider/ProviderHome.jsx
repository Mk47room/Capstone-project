import ClaimList from "./ClaimList";
import Stat from "./Stat";
import WelcomeProvider from "./WelcomeProvider";

function ProviderHome() {

    return (
        <div>
            <div className="mb-4">
                <WelcomeProvider />
            </div>
            <Stat />
            <ClaimList />
        </div>
    )
}

export default ProviderHome