package com.pq.user.service.impl;

import com.pq.common.constants.CommonConstants;
import com.pq.common.exception.CommonErrors;
import com.pq.common.util.DateUtil;
import com.pq.user.dto.DynamicCommentDto;
import com.pq.user.dto.DynamicPraiseDto;
import com.pq.user.dto.UserDynamicDto;
import com.pq.user.entity.UserDynamic;
import com.pq.user.entity.UserDynamicComment;
import com.pq.user.entity.UserDynamicImg;
import com.pq.user.entity.UserDynamicPraise;
import com.pq.user.exception.UserErrorCode;
import com.pq.user.exception.UserException;
import com.pq.user.feign.AgencyFeign;
import com.pq.user.form.CancelPraiseDynamicForm;
import com.pq.user.form.PraiseDynamicForm;
import com.pq.user.form.UserDynamicCommentForm;
import com.pq.user.form.UserDynamicForm;
import com.pq.user.mapper.UserDynamicCommentMapper;
import com.pq.user.mapper.UserDynamicImgMapper;
import com.pq.user.mapper.UserDynamicMapper;
import com.pq.user.mapper.UserDynamicPraiseMapper;
import com.pq.user.service.UserDynamicService;
import com.pq.user.utils.ConstantsUser;
import com.pq.user.utils.UserResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    public List<UserDynamicDto> getUserDynamicList(Long agencyClassId,String userId, int offset, int size){

        List<UserDynamicDto> dynamicDtoList = new ArrayList<>();
        List<UserDynamic> userDynamicList = userDynamicMapper.selectUserClassDynamicByClassId(agencyClassId,offset,size);
        for(UserDynamic userDynamic : userDynamicList){

            UserDynamicDto userDynamicDto = new UserDynamicDto();
            userDynamicDto.setId(userDynamic.getId());
            userDynamicDto.setUserId(userDynamic.getUserId());
            userDynamicDto.setName(userDynamic.getName());
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

            List<UserDynamicPraise> praiseList = userDynamicPraiseMapper.selectByDynamicId(userDynamic.getId());
            List<DynamicPraiseDto> dynamicPraiseDtoList = new ArrayList<>();
            for(UserDynamicPraise userDynamicPraise:praiseList){
                if(userId.equals(userDynamicPraise.getUserId())){
                    userDynamicDto.setPraiseState(1);
                }
                DynamicPraiseDto dynamicPraiseDto = new DynamicPraiseDto();
                dynamicPraiseDto.setId(userDynamicPraise.getId());
                dynamicPraiseDto.setName(userDynamicPraise.getName());
                dynamicPraiseDto.setUserId(userDynamicPraise.getUserId());
                dynamicPraiseDtoList.add(dynamicPraiseDto);
            }
            userDynamicDto.setPraiseList(dynamicPraiseDtoList);
            List<UserDynamicComment> commentList = userDynamicCommentMapper.selectByDynamicId(userDynamic.getId());
            List<DynamicCommentDto> dynamicCommentDtos = new ArrayList<>();
            for(UserDynamicComment dynamicComment:commentList){
                DynamicCommentDto dynamicCommentDto = new DynamicCommentDto();
                dynamicCommentDto.setId(dynamicComment.getId());
                dynamicCommentDto.setDynamicId(dynamicComment.getDynamicId());
                dynamicCommentDto.setOriginatorUserId(dynamicComment.getOriginatorUserId());
                dynamicCommentDto.setOriginatorName(dynamicComment.getOriginatorName());
                dynamicCommentDto.setReceiverUserId(dynamicComment.getReceiverUserId());
                dynamicCommentDto.setReceiverName(dynamicComment.getReceiverName());
                dynamicCommentDto.setContent(dynamicComment.getContent());
                dynamicCommentDtos.add(dynamicCommentDto);
            }
            userDynamicDto.setCommentList(dynamicCommentDtos);

            dynamicDtoList.add(userDynamicDto);
        }
        return dynamicDtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDynamic(UserDynamicForm userDynamicForm){
        UserDynamic userDynamic = new UserDynamic();
        userDynamic.setUserId(userDynamicForm.getUserId());
        userDynamic.setAgencyClassId(userDynamicForm.getAgencyClassId());
        userDynamic.setName(userDynamicForm.getName());
        userDynamic.setContent(userDynamicForm.getContent());
        userDynamic.setPraiseCount(0);
        userDynamic.setCommentCount(0);
        userDynamic.setState(CommonConstants.PQ_STATE_VALID);
        userDynamic.setCreatedTime(DateUtil.currentTime());
        userDynamic.setUpdatedTime(DateUtil.currentTime());
        userDynamicMapper.insert(userDynamic);

        for(String img : userDynamicForm.getImgList()){
            UserDynamicImg userDynamicImg = new UserDynamicImg();
            userDynamicImg.setDynamicId(userDynamic.getId());
            userDynamicImg.setImg(img);
            userDynamicImg.setState(CommonConstants.PQ_STATE_VALID);
            userDynamicImg.setCreatedTime(DateUtil.currentTime());
            userDynamicImg.setUpdatedTime(DateUtil.currentTime());
            userDynamicImgMapper.insert(userDynamicImg);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long praiseDynamic(PraiseDynamicForm praiseDynamicForm){
        UserDynamicPraise dynamicPraise = new UserDynamicPraise();
        dynamicPraise.setDynamicId(praiseDynamicForm.getDynamicId());
        dynamicPraise.setUserId(praiseDynamicForm.getUserId());
        dynamicPraise.setName(praiseDynamicForm.getName());
        dynamicPraise.setState(CommonConstants.PQ_STATE_VALID);
        dynamicPraise.setUpdatedTime(DateUtil.currentTime());
        dynamicPraise.setCreatedTime(DateUtil.currentTime());
        userDynamicPraiseMapper.insert(dynamicPraise);

        userDynamicMapper.addPraiseCountById(dynamicPraise.getDynamicId());
        return dynamicPraise.getId();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPraiseDynamic(CancelPraiseDynamicForm cancelPraiseDynamicForm){
        UserDynamicPraise dynamicPraise = userDynamicPraiseMapper.selectByPrimaryKey(cancelPraiseDynamicForm.getPraiseId());
        dynamicPraise.setState(CommonConstants.PQ_STATE_UN_VALID);
        dynamicPraise.setUpdatedTime(DateUtil.currentTime());
        userDynamicPraiseMapper.updateByPrimaryKey(dynamicPraise);

        userDynamicMapper.subPraiseCountById(dynamicPraise.getDynamicId());
    }

    @Override
    public Long createDynamicComment(UserDynamicCommentForm dynamicCommentForm){
        UserDynamicComment userDynamicComment = new UserDynamicComment();
        BeanUtils.copyProperties(dynamicCommentForm,userDynamicComment);
        userDynamicComment.setState(CommonConstants.PQ_STATE_VALID);
        userDynamicComment.setCreatedTime(DateUtil.currentTime());
        userDynamicComment.setUpdatedTime(DateUtil.currentTime());
        userDynamicCommentMapper.insert(userDynamicComment);
        return userDynamicComment.getId();
    }


}
