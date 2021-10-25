package daedan.mes.file.repository;

import daedan.mes.file.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository  extends JpaRepository<FileInfo, Long> {

    FileInfo findByCustNoAndSaveFileNmAndUsedYn(Long custNo, String saveFileNm,String yn);
    FileInfo findByCustNoAndFileNoAndUsedYn(Long custNo,Long fileNo,String yn);

    FileInfo findByCustNoAndFileNoAndUsedYn(Long custNo, long fileNo, String y);
}
