import axios from "axios"
import { useEffect, useState } from "react"
import { API_SERVER_URL_DEV } from "../../../config"

function PatientPolicy() {

    const [page, setPage] = useState(0)
    const [size, setSize] = useState(5)
    const [totalPages, setTotalPages] = useState(0)
    const [totalRecords, setTotalRecords] = useState(0)
    const [currentPageSize, setCurrentPageSize] = useState(0)

    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    const [list, setList] = useState([])
    const [planId, setPlanId] = useState(0)
    const [years, setYears] = useState(1)

    useEffect(() => {
        const getPolicy = async () => {
            try {
                const response = await axios.get(`${API_SERVER_URL_DEV}/api/policy/all?page=${page}&size=${size}`, {
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                })

                setList(response.data.list)
                setTotalPages(response.data.paginationInfo.totalPages)
                setTotalRecords(response.data.paginationInfo.totalRecords)
                setCurrentPageSize(response.data.paginationInfo.numRecordsInPage)

            } catch (err) {
                setErrMsg(err?.response?.data?.message)
                setErrComment(err?.response?.data?.comment)
            }



        }
        getPolicy()
    }, [page, size])

    const onAddPolicy = async () => {
        try {
            if (planId === 0 || years <= 0) {
                throw new Error("ID and year are needed.");
            }
            await axios.post(`${API_SERVER_URL_DEV}/api/policy/add/${planId}?policyYears=${years}`,{},{
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                }
            );
            alert("Policy Added successfully!");
        } catch (err) {
            setErrMsg(err?.response?.data?.message || err.message);
            setErrComment(err?.response?.data?.comment);
        }


    }

    const onPageChange = (val) => {
        setPage(val)
    }

    return (
        <div>
            <div className="card border-0 shadow-sm">

                <div className="card-header bg-white d-flex justify-content-between align-items-center py-3">
                    <h5 className="mb-0">My Policies</h5>
                    <button className="btn btn-primary" onClick={() => {
                        setPlanId(0)
                        setYears(1)
                        setErrMsg("")
                        setErrComment("")
                    }} data-bs-toggle="modal" data-bs-target="#policyModal">+ Take New Policy</button>
                </div>

                {/* <!-- Error / Header --> */}
                <div className="px-3">
                    <p className="text-danger fw-semibold mb-0">
                        {errMsg}
                    </p>
                    <p className="text-muted small mb-0">
                        {errComment}
                    </p>
                </div>

                <div className="table-responsive">
                    <table className="table table-stripped align-middle mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>S.No</th>
                                <th>Policy Id</th>
                                <th>Starting Date</th>
                                <th>Ending Date</th>
                                <th className="text-end">Premium Amount (₹)</th>
                                <th className="text-end">Coverage Amount (₹)</th>
                                <th>Plan ID</th>
                                <th>Plan Name</th>
                            </tr>
                        </thead>
                        <tbody>
                            {list?.map((p, index) => (
                                <tr key={index}>
                                    <th>{index + 1}</th>
                                    <td>{p.policyId}</td>
                                    <td>{p.startingDate}</td>
                                    <td>{p.endingDate}</td>
                                    <td className="text-end">{p.premiumAmount}</td>
                                    <td className="text-end">{p.coverageAmount}</td>
                                    <td>{p.InsurancePlanId}</td>
                                    <td>{p.InsurancePlanName}</td>
                                </tr>
                            ))}

                        </tbody>
                    </table>
                    <nav aria-label="Page navigation example" style={{ "alignContent": 'center' }}>
                        <ul className="pagination justify-content-center">
                            <li className="page-item"><button className="page-link" onClick={() => setPage(page - 1)}
                                disabled={page === 0}>Previous</button></li>

                            {
                                Array.from({ length: totalPages }, (e, index) => (
                                    <li key={index} className="page-item">
                                        <button className="page-link" onClick={() => onPageChange(index)}>{index + 1}</button></li>
                                ))}

                            <li className="page-item"><button className="page-link" onClick={() => setPage(page + 1)}
                                disabled={page === (totalPages - 1)} >Next</button></li>

                        </ul>
                        <p>{`Page ${page + 1} of ${totalPages} total pages`}</p>
                        <p>{`Showing ${currentPageSize} records from total ${totalRecords} records`}</p>
                    </nav>
                </div>
            </div>
            <div className="modal fade" id="policyModal" data-bs-backdrop="static" data-bs-keyboard="false"
                tabIndex="-1" aria-labelledby="policyModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content border-0 shadow">
                        <div className="modal-header">
                            <h5 className="modal-title" id="policyModalLabel">
                                New Policy
                            </h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" />
                        </div>
                        <div className="modal-body">
                            <div className="mb-3">
                                <label className="form-label">Plan ID</label>
                                <input type="number" value={planId} onChange={(e) => setPlanId(e.target.value)} className="form-control" />
                                <div className="form-text">Your Plan id.</div>
                            </div>
                            <div className="mb-3">
                                <label className="form-label">How Many Years?</label>
                                <input type="number" value={years} onChange={(e) => setYears(e.target.value)} className="form-control" />
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-light" data-bs-dismiss="modal">
                                Close
                            </button>
                            <button type="button" className="btn btn-primary" onClick={onAddPolicy} data-bs-dismiss="modal">
                                Submit request
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default PatientPolicy