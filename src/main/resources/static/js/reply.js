
async function getList({bno, page, size,goLast}){
    const result = await axios.get(`/replies/list/${bno}`,{param:{page,size}})
    return result.data
}