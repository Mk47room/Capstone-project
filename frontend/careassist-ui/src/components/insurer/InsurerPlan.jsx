import axios from "axios"
import { use, useEffect, useRef, useState } from "react"
import { API_SERVER_URL_DEV } from "../../../config"

function InsurerPlan() {

    const [page, setPage] = useState(0)
    const [size, setSize] = useState(5)
    const [totalPages, setTotalPages] = useState(0)
    const [totalRecords, setTotalRecords] = useState(0)
    const [currentPageSize, setCurrentPageSize] = useState(0)

    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    const [list, setList] = useState([])
    const [planId,setPlanId] = useState("")
    const [name, setName] = useState("")
    const [basePremium, setBasePremium] = useState("")
    const [coverageAmount, setCoverageAmount] = useState("")
    const [ageMin, setAgeMin] = useState("")
    const [ageMax, setAgeMax] = useState("")
    const [age, setAge] = useState("")

    const [flag, setFlag] = useState(false)

    const closeBtnRef = useRef(null)

    useEffect(() => {
        const getPlans = async () => {

            try {

                const response = await axios.get(`${API_SERVER_URL_DEV}/api/insuranceplan/all?basePremium=${basePremium}&coverageAmount=${coverageAmount}&age=${age}&page=${page}&size=${size}`, {
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                })
                setList(response.data.list)
                setTotalPages(response.data.paginationInfo.totalPages)
                setTotalRecords(response.data.paginationInfo.totalRecords)
                setCurrentPageSize(response.data.paginationInfo.numRecordsInPage)

                // console.log(response.data)
            } catch (err) {
                setErrMsg(err?.response?.data?.message)
                setErrComment(err?.response?.data?.comment)
                // console.log(err)
            }

        }

        getPlans()
    }, [basePremium, coverageAmount, age, page, size])

    const onAddPlan = async (e) => {
        e.preventDefault()

        let obj = {
            "name": name,
            "basePremium": basePremium,
            "coverageAmount": coverageAmount,
            "ageMin": ageMin,
            "ageMax": ageMax
        }
        try {
            await axios.post(API_SERVER_URL_DEV + "/api/insuranceplan/add", obj, {
                headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
            })

            alert("Plan added Successfully!")

        } catch (err) {
            setErrMsg(err?.response?.data?.message)
            setErrComment(err?.response?.data?.comment)
        }
    }

    const onEditPlan = async (e) => {
        e.preventDefault()

        let obj = {
            "name": name,
            "basePremium": basePremium,
            "coverageAmount": coverageAmount,
            "ageMin": ageMin,
            "ageMax": ageMax
        }

        try {
            await axios.put(`${API_SERVER_URL_DEV}/api/insuranceplan/update/${planId}`,obj,{
            headers : {Authorization : 'Bearer ' + localStorage.getItem('token')}
        })
        alert("Plan Saved!")

        } catch (err) {
            setErrMsg(err?.response?.data?.message)
            setErrComment(err?.response?.data?.comment)
        }

        

    }

    const onEdit = (e, plan) => {
        e.preventDefault()
        setFlag(false)
        setPlanId(plan.planId)
        setName(plan.name),
        setBasePremium(plan.basePremium),
        setCoverageAmount(plan.coverageAmount),
        setAgeMin(plan.ageMin),
        setAgeMax(plan.ageMax)
    }
    const reset = (e) => {
        e.preventDefault()
        setFlag(true)
        setName(""), setErrMsg(""), setErrComment(""),
            setBasePremium(""), setCoverageAmount(""), setAgeMin(""), setAgeMax("")
    }

    const onPageChange = (val) => {
        setPage(val)
    }

    return (
        <div>
            <div className="card border-0 shadow-sm">

                <div className="card-header bg-white d-flex justify-content-between align-items-center py-3">
                    <h5 className="mb-0">Plan List</h5>
                    <button className="btn btn-primary" onClick={(e) => reset(e)} data-bs-toggle="modal" data-bs-target="#planModal">+ New Plan</button>
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
                    <table className="table table-hover align-middle mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>S.No</th>
                                <th>Plan ID</th>
                                <th>Plan Name</th>
                                <th className="text-end">Base Premium (₹)</th>
                                <th className="text-end">Coverage Amount (₹)</th>
                                <th>Age Min</th>
                                <th>Age Max</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th></th>
                                <th></th>
                                <th></th>
                                <th className="text-end">
                                    <input type="number" placeholder="Minimun Base Premium" onChange={(e) => setBasePremium(e.target.value)} className="form-control" />
                                </th>
                                <th className="text-end">
                                    <input type="number" placeholder="Minimum Coverage Amount" onChange={(e) => setCoverageAmount(e.target.value)} className="form-control" />
                                </th>
                                <th>
                                    <input type="number" placeholder="Enter Age" onChange={(e) => setAge(e.target.value)} className="form-control" />
                                </th>
                            </tr>
                            {list?.map((p, index) => (
                                <tr key={index}>
                                    <th>{index + 1}</th>
                                    <td>{p.planId}</td>
                                    <td>{p.name}</td>
                                    <td className="text-end">{p.basePremium}</td>
                                    <td className="text-end">{p.coverageAmount}</td>
                                    <td>{p.ageMin}</td>
                                    <td>{p.ageMax}</td>
                                    <td>
                                        <button className="btn btn-info" onClick={(e) => onEdit(e, p)} data-bs-toggle="modal" data-bs-target="#planModal" >Edit</button>
                                    </td>
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
            <div className="modal fade" id="planModal" data-bs-backdrop="static" data-bs-keyboard="false"
                tabIndex="-1" aria-labelledby="planModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content border-0 shadow">

                        <form >
                            <div className="modal-header">
                                <h5 className="modal-title" id="planModalLabel">
                                    Plan Details
                                </h5>
                                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" onClick={(e) => reset(e)} ref={closeBtnRef} />
                            </div>
                            <div className="modal-body">
                                <div className="mb-3">
                                    <label className="form-label">Plan Name</label>
                                    <input type="text" value={name} onChange={(e) => setName(e.target.value)} className="form-control" required />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Base Premium</label>
                                    <input type="number" value={basePremium} onChange={(e) => setBasePremium(e.target.value)} className="form-control" required />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Coverage Amount</label>
                                    <input type="number" value={coverageAmount} onChange={(e) => setCoverageAmount(e.target.value)} className="form-control" required />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Age Min</label>
                                    <input type="number" value={ageMin} onChange={(e) => setAgeMin(e.target.value)} className="form-control" required />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Age Max</label>
                                    <input type="number" value={ageMax} onChange={(e) => setAgeMax(e.target.value)} className="form-control" required />
                                </div>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-light" data-bs-dismiss="modal" onClick={(e) => reset(e)} >
                                    Close
                                </button>
                                {flag ?
                                    <button type="submit" className="btn btn-primary" onClick={(e) => onAddPlan(e)}>Add Plan</button>
                                    :
                                    <button type="submit" className="btn btn-primary" onClick={(e) => onEditPlan(e)}>Save Plan</button>
                                }

                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    )
}

export default InsurerPlan