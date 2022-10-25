package top.asshell.jr.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.asshell.jr.entiy.AddressBook;
import top.asshell.jr.mapper.AddressBookMapper;
import top.asshell.jr.service.AddressBookService;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
