package com.pq.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.pq.common.constants.CommonConstants;
import com.pq.common.exception.CommonErrors;
import com.pq.common.util.DateUtil;
import com.pq.common.util.HttpUtil;
import com.pq.common.util.StringUtil;
import com.pq.user.dto.*;
import com.pq.user.entity.*;
import com.pq.user.exception.UserErrorCode;
import com.pq.user.exception.UserErrors;
import com.pq.user.exception.UserException;
import com.pq.user.feign.AgencyFeign;
import com.pq.user.form.CancelPraiseDynamicForm;
import com.pq.user.form.PraiseDynamicForm;
import com.pq.user.form.UserDynamicCommentForm;
import com.pq.user.form.UserDynamicForm;
import com.pq.user.mapper.*;
import com.pq.user.service.UserDynamicService;
import com.pq.user.utils.ConstantsUser;
import com.pq.user.utils.UserResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author liutao
 */
@Service
public class UserDynamicServiceImpl implements UserDynamicService {
    @Autowired
    private UserDynamicMapper userDynamicMapper;
    @Autowired
    private UserDynamicImgMapper userDynamicImgMapper;
    @Autowired
    private UserDynamicPraiseMapper userDynamicPraiseMapper;
    @Autowired
    private UserDynamicCommentMapper userDynamicCommentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AgencyFeign agencyFeign;

    @Value("${php.url}")
    private String phpUrl;

    @Override
    public List<UserDynamicDto> getUserDynamicList(Long agencyClassId,Long studentId,String userId, int offset, int size){

        List<UserDynamicDto> dynamicDtoList = new ArrayList<>();
        List<UserDynamic> userDynamicList = userDynamicMapper.selectUserClassDynamicByClassId(agencyClassId,offset,size);
        for(UserDynamic userDynamic : userDynamicList){

            UserDynamicDto userDynamicDto = new UserDynamicDto();
            userDynamicDto.setId(userDynamic.getId());
            userDynamicDto.setUserId(userDynamic.getUserId());
            userDynamicDto.setName(userDynamic.getName());
            userDynamicDto.setAvatar(userMapper.selectByUserId(userDynamic.getUserId()).getAvatar());
            userDynamicDto.setContent(userDynamic.getContent());
            userDynamicDto.setPraiseCount(userDynamic.getPraiseCount());
            userDynamicDto.setCommentCount(userDynamic.getCommentCount());
            userDynamicDto.setCreatedTime(DateUtil.formatDate(userDynamic.getCreatedTime(),DateUtil.DEFAULT_DATETIME_FORMAT));

            List<UserDynamicImg> imgList = userDynamicImgMapper.selectByDynamicId(userDynamic.getId());
            List<String> imgs = new ArrayList<>();
            for(UserDynamicImg userDynamicImg : imgList){
                if(userDynamicImg.getType()==ConstantsUser.USER_DYNAMIC_IMG_TYPE_IMG){
                    imgs.add(userDynamicImg.getImg());
                }else {
                    userDynamicDto.setMovieUrl(userDynamicImg.getImg());
                }
            }
            userDynamicDto.setImgList(imgs);

            userDynamicDto.setPraiseList(getDynamicPraiseDtoList(userDynamic));
            for(DynamicPraiseDto dynamicPraiseDto:userDynamicDto.getPraiseList()){
                if(studentId==null || studentId==0){
                    if(userId.equals(dynamicPraiseDto.getUserId())){
                        userDynamicDto.setPraiseState(1);
                    }
                }else {
                    if(userId.equals(dynamicPraiseDto.getUserId())&& studentId.equals(dynamicPraiseDto.getStudentId())){
                        userDynamicDto.setPraiseState(1);
                    }
                }
            }

            userDynamicDto.setCommentList(getDynamicCommentDtolist(userDynamic));

            dynamicDtoList.add(userDynamicDto);
        }
        return dynamicDtoList;
    }

    private List<DynamicPraiseDto> getDynamicPraiseDtoList(UserDynamic userDynamic){
        List<DynamicPraiseDto> dynamicPraiseDtoList = new ArrayList<>();
        List<UserDynamicPraise> praiseList = userDynamicPraiseMapper.selectByDynamicId(userDynamic.getId());
        for(UserDynamicPraise userDynamicPraise:praiseList){
            DynamicPraiseDto dynamicPraiseDto = new DynamicPraiseDto();
            dynamicPraiseDto.setId(userDynamicPraise.getId());
            dynamicPraiseDto.setName(userDynamicPraise.getName());
            dynamicPraiseDto.setUserId(userDynamicPraise.getUserId());
            dynamicPraiseDto.setStudentId(userDynamicPraise.getStudentId());
            dynamicPraiseDtoList.add(dynamicPraiseDto);
        }
        return dynamicPraiseDtoList;
    }

    private List<DynamicCommentDto> getDynamicCommentDtolist(UserDynamic userDynamic){
        List<UserDynamicComment> commentList = userDynamicCommentMapper.selectByDynamicId(userDynamic.getId());
        List<DynamicCommentDto> dynamicCommentDtos = new ArrayList<>();
        for(UserDynamicComment dynamicComment:commentList){
            DynamicCommentDto dynamicCommentDto = new DynamicCommentDto();
            dynamicCommentDto.setId(dynamicComment.getId());
            dynamicCommentDto.setDynamicId(dynamicComment.getDynamicId());
            dynamicCommentDto.setOriginatorUserId(dynamicComment.getOriginatorUserId());
            dynamicCommentDto.setOriginatorName(dynamicComment.getOriginatorName());
            dynamicCommentDto.setOriginatorStudentId(dynamicComment.getOriginatorStudentId());
            dynamicCommentDto.setReceiverUserId(dynamicComment.getReceiverUserId());
            dynamicCommentDto.setReceiverName(dynamicComment.getReceiverName());
            dynamicCommentDto.setReceiverStudentId(dynamicComment.getReceiverStudentId());
            dynamicCommentDto.setContent(dynamicComment.getContent());
            dynamicCommentDtos.add(dynamicCommentDto);
        }
        return dynamicCommentDtos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDynamic(UserDynamicForm userDynamicForm){
        UserDynamic userDynamic = new UserDynamic();
        userDynamic.setUserId(userDynamicForm.getUserId());
        userDynamic.setAgencyClassId(userDynamicForm.getAgencyClassId());
        userDynamic.setStudentId(userDynamicForm.getStudentId());
        userDynamic.setName(userDynamicForm.getName());
        userDynamic.setContent(userDynamicForm.getContent());
        userDynamic.setPraiseCount(0);
        userDynamic.setCommentCount(0);
        userDynamic.setState(CommonConstants.PQ_STATE_VALID);
        userDynamic.setCreatedTime(DateUtil.currentTime());
        userDynamic.setUpdatedTime(DateUtil.currentTime());
        userDynamicMapper.insert(userDynamic);

        for(String img : userDynamicForm.getImgList()){
            createImg(img,userDynamic.getId(),ConstantsUser.USER_DYNAMIC_IMG_TYPE_IMG);
        }
        if(!StringUtil.isEmpty(userDynamicForm.getMovieUrl())){
            createImg(userDynamicForm.getMovieUrl(),userDynamic.getId(),ConstantsUser.USER_DYNAMIC_IMG_TYPE_MOVIE);
        }

        UserResult<List<UserDto>> result = agencyFeign.getAllAgencyClassUser(userDynamicForm.getAgencyClassId());
        if(!CommonErrors.SUCCESS.getErrorCode().equals(result.getStatus())){
            throw new UserException(new UserErrorCode(result.getStatus(),result.getMessage()));
        }
        User fromUser = userMapper.selectByUserId(userDynamicForm.getUserId());


        for(UserDto agencyUser: result.getData()){
            if(agencyUser.getUserId().equals(userDynamicForm.getUserId())){
                continue;
            }
            User user = userMapper.selectByUserId(agencyUser.getUserId());

            HashMap<String, Object> paramMap = new HashMap<>();

            paramMap.put("parameterId", ConstantsUser.PUSH_TEMPLATE_ID_NOTICE_9);
            paramMap.put("user", user.getHuanxinId());
            paramMap.put("form", fromUser.getHuanxinId());
            paramMap.put("userName", userDynamicForm.getName());

            String huanxResult = null;
            try {
                huanxResult = HttpUtil.sendJson(phpUrl+"push",new HashMap<>(),JSON.toJSONString(paramMap));
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserResult userResult = JSON.parseObject(huanxResult,UserResult.class);
            if(userResult==null||!CommonErrors.SUCCESS.getErrorCode().equals(userResult.getStatus())){
                UserException.raise(UserErrors.USER_DYNAMIC_NOTICE_PUSH_ERROR);
            }
        }

    }
    private void createImg(String img,Long id,int type){
        UserDynamicImg userDynamicImg = new UserDynamicImg();
        userDynamicImg.setDynamicId(id);
        userDynamicImg.setImg(img);
        userDynamicImg.setType(type);
        userDynamicImg.setState(CommonConstants.PQ_STATE_VALID);
        userDynamicImg.setCreatedTime(DateUtil.currentTime());
        userDynamicImg.setUpdatedTime(DateUtil.currentTime());
        userDynamicImgMapper.insert(userDynamicImg);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PraiseDto praiseDynamic(PraiseDynamicForm praiseDynamicForm){

        UserDynamicPraise dynamicPraise = userDynamicPraiseMapper.
                selectByDynamicIdAndUserIdAndStudentId(praiseDynamicForm.getDynamicId(),praiseDynamicForm.getUserId(),
                        praiseDynamicForm.getStudentId());
        if(dynamicPraise!=null){
            UserException.raise(UserErrors.USER_PRAISE_IS_EXIST_ERROR);
        }
        dynamicPraise = new UserDynamicPraise();
        dynamicPraise.setDynamicId(praiseDynamicForm.getDynamicId());
        dynamicPraise.setUserId(praiseDynamicForm.getUserId());
        dynamicPraise.setStudentId(praiseDynamicForm.getStudentId());
        dynamicPraise.setName(praiseDynamicForm.getName());
        dynamicPraise.setState(CommonConstants.PQ_STATE_VALID);
        dynamicPraise.setUpdatedTime(DateUtil.currentTime());
        dynamicPraise.setCreatedTime(DateUtil.currentTime());
        userDynamicPraiseMapper.insert(dynamicPraise);

        userDynamicMapper.addPraiseCountById(dynamicPraise.getDynamicId());

        UserDynamic userDynamic = userDynamicMapper.selectByPrimaryKey(dynamicPraise.getDynamicId());
        PraiseDto praiseDto = new PraiseDto();
        praiseDto.setList(getDynamicPraiseDtoList(userDynamic));
        praiseDto.setPraiseCount(userDynamic.getPraiseCount());
        return praiseDto;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PraiseDto cancelPraiseDynamic(CancelPraiseDynamicForm cancelPraiseDynamicForm){
        UserDynamicPraise dynamicPraise = userDynamicPraiseMapper.selectByDynamicIdAndUserIdAndStudentId(cancelPraiseDynamicForm.getDynamicId(),
                cancelPraiseDynamicForm.getUserId(),cancelPraiseDynamicForm.getStudentId());

        dynamicPraise.setState(CommonConstants.PQ_STATE_UN_VALID);
        dynamicPraise.setUpdatedTime(DateUtil.currentTime());
        userDynamicPraiseMapper.updateByPrimaryKey(dynamicPraise);

        userDynamicMapper.subPraiseCountById(dynamicPraise.getDynamicId());

        UserDynamic userDynamic = userDynamicMapper.selectByPrimaryKey(dynamicPraise.getDynamicId());
        PraiseDto praiseDto = new PraiseDto();
        praiseDto.setList(getDynamicPraiseDtoList(userDynamic));
        praiseDto.setPraiseCount(userDynamic.getPraiseCount());
        return praiseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentDto createDynamicComment(UserDynamicCommentForm dynamicCommentForm){
        UserDynamicComment userDynamicComment = new UserDynamicComment();
        BeanUtils.copyProperties(dynamicCommentForm,userDynamicComment);
        userDynamicComment.setState(CommonConstants.PQ_STATE_VALID);
        userDynamicComment.setCreatedTime(DateUtil.currentTime());
        userDynamicComment.setUpdatedTime(DateUtil.currentTime());
        userDynamicCommentMapper.insert(userDynamicComment);

        userDynamicMapper.addCommentCountById(userDynamicComment.getDynamicId());

        UserDynamic userDynamic = userDynamicMapper.selectByPrimaryKey(userDynamicComment.getDynamicId());
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentCount(userDynamic.getCommentCount());
        commentDto.setList(getDynamicCommentDtolist(userDynamic));
        return commentDto;
    }
    @Override
    public void deleteDynamic(Long id, String userId,Long studentId,int role){
        UserDynamic userDynamic = userDynamicMapper.selectByPrimaryKey(id);
        if(userDynamic==null){
            UserException.raise(UserErrors.USER_DYNAMIC_NOT_EXIST_ERROR);
        }
        if(!userId.equals(userDynamic.getUserId())){
            UserException.raise(UserErrors.USER_DYNAMIC_CAN_NOT_DELETE_ERROR);
        }
        if(role==CommonConstants.PQ_LOGIN_ROLE_PARENT){
            if(!studentId.equals(userDynamic.getStudentId())){
                UserException.raise(UserErrors.USER_DYNAMIC_CAN_NOT_DELETE_ERROR);
            }
        }

        userDynamic.setState(CommonConstants.PQ_STATE_UN_VALID);
        userDynamic.setUpdatedTime(DateUtil.currentTime());
        userDynamicMapper.updateByPrimaryKey(userDynamic);
    }



}
