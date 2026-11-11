import { useEffect } from "react"

function PageNotFound() {
    useEffect(() => {
        localStorage.clear()
    }, [])

    return (
        <div>
            <h4>Page Not Found</h4>
        </div>
    )
}
export default PageNotFound