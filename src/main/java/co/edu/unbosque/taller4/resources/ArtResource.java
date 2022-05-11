//package co.edu.unbosque.taller4.resources;
//
//import co.edu.unbosque.taller4.services.ArtPieceService;
//import org.jboss.resteasy.plugins.providers.multipart.InputPart;
//import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
//
//import javax.servlet.ServletContext;
//import javax.ws.rs.*;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.core.Response;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Path("/arts")
//public class ArtResource {
//
//    @Context
//    ServletContext context;
//
//    private final String UPLOAD_DIRECTORY = File.separator + "arts";
//
//    @POST
//    @Path("/{username}")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response uploadFile(MultipartFormDataInput input,
//                               @PathParam("username") String username) {
//        String fileName = "";
//
//        try {
//            // Getting the file from form input
//            Map<String, List<InputPart>> formParts = input.getFormDataMap();
//            String title = formParts.get("title").get(0).getBodyAsString();
//            String price = formParts.get("price").get(0).getBodyAsString();
//            List<InputPart> inputParts = formParts.get("image");
//
//            String theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvwxyz";
//            StringBuilder builder;
//
//            builder = new StringBuilder(16);
//
//            for (int m = 0; m < 16; m++) {
//                int myindex = (int)(theAlphaNumericS.length() * Math.random());
//
//                builder.append(theAlphaNumericS.charAt(myindex));
//            }
//            String newFileName = builder.toString();
//
//            String csvPath = context.getRealPath("") + File.separator + "WEB-INF/classes/"+"arts.csv";
//            String uploadPath = context.getRealPath("") + File.separator+"arts";
//
//            boolean createArt = new ArtPieceService().createArt(username,title,price,newFileName,csvPath ).get();
//
//            if (createArt){
//                for (InputPart inputPart : inputParts){
//                    if (fileName.equals("") || fileName == null) {
//                        // Retrieving headers and reading the Content-Disposition header to file name
//                        MultivaluedMap<String, String> headers = inputPart.getHeaders();
//                        fileName = parseFileName(headers);
//                    }
//
//                    String format = fileName.split("\\.")[1];
//
//                    newFileName += "."+format;
//
//                    InputStream inputStream = inputPart.getBody(InputStream.class,null);
//
//                    saveFile(inputStream,newFileName,context);
//                }
//            }
//            else{
//                System.out.println("Error");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return Response.ok()
//                .entity("Image successfully uploaded")
//                .build();
//    }
//
//    private String parseFileName(MultivaluedMap<String, String> headers) {
//        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");
//
//        for (String name : contentDispositionHeader) {
//            if ((name.trim().startsWith("filename"))) {
//                String[] tmp = name.split("=");
//                String fileName = tmp[1].trim().replaceAll("\"","");
//                return fileName;
//            }
//        }
//
//        return "unknown";
//    }
//
//    // Save uploaded file to a defined location on the server
//    private void saveFile(InputStream uploadedInputStream, String fileName, ServletContext context) {
//        int read = 0;
//        byte[] bytes = new byte[1024];
//
//        try {
//            // Complementing servlet path with the relative path on the server
//            String uploadPath = context.getRealPath("") + UPLOAD_DIRECTORY;
//
//            // Creating the upload folder, if not exist
//            File uploadDir = new File(uploadPath);
//            if (!uploadDir.exists()) uploadDir.mkdir();
//
//            // Persisting the file by output stream
//            OutputStream outpuStream = new FileOutputStream(uploadPath + fileName);
//            while ((read = uploadedInputStream.read(bytes)) != -1) {
//                outpuStream.write(bytes, 0, read);
//            }
//
//            outpuStream.flush();
//            outpuStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @POST
//    @Path("/collections")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response createCollection(@FormParam("coleccion")String coleccion){
//        String UPLOAD_DIRECTORY_COLLECTIONS = context.getRealPath("") + File.separator+"arts"+File.separator+coleccion;
//
//        File collection = new File(UPLOAD_DIRECTORY_COLLECTIONS);
//        if (!collection.exists())collection.mkdir();
//        else{
//            return Response.ok().entity(coleccion).build();
//        }
//        return Response.serverError().build();
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getArtsImage(){
//
//        String path = context.getRealPath("") +UPLOAD_DIRECTORY;
//
//        File uploadDir = new File(path);
//
//        List<String> files = new ArrayList<String>();
//        for (File file : uploadDir.listFiles()) {
//            files.add(UPLOAD_DIRECTORY + File.separator + file.getName());
//        }
//
//        return Response.status(200).entity(files).build();
//    }
//
//}