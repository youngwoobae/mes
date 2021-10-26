package daedan.mes.file.service;

import daedan.mes.file.domain.FileInfo;

import java.util.Map;

public interface FileService {
    Long saveFile(FileInfo fileEntity);
    String getFileInfo(String fileRoot, Long fileNo);
    FileInfo getFileVo(Long custNo, Long fileNo);
    void revivalFileUsed(Map<String, Object> paraMap);
    void dropFile(String absFile);
    String getApndFileUrl(Map<String, Object> paraMap);
    String getImageUrl(Map<String, Object> paraMap);
}
