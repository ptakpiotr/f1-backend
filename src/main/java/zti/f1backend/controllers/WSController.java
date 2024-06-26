package zti.f1backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import zti.f1backend.data.CommentsRepository;
import zti.f1backend.data.RaceScoreRepository;
import zti.f1backend.data.UserCommentsRepository;
import zti.f1backend.data.UserRepository;
import zti.f1backend.models.Comments;
import zti.f1backend.models.GeneralizedWSResponse;
import zti.f1backend.models.RaceScore;
import zti.f1backend.models.User;
import zti.f1backend.models.UserComments;
import zti.f1backend.models.dto.ChangeLikeDTO;
import zti.f1backend.models.dto.ChangeScoreDTO;
import zti.f1backend.models.dto.ChangedScoreDTO;
import zti.f1backend.models.dto.CommentCreateDTO;
import zti.f1backend.models.dto.CommentDeleteDTO;
import zti.f1backend.models.dto.CommentDeletedDTO;
import zti.f1backend.models.dto.GetCommentsDTO;

@Controller
public class WSController {
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final RaceScoreRepository raceScoreRepository;
    private final UserCommentsRepository userCommentsRepository;

    public WSController(CommentsRepository commentsRepository,
            UserRepository userRepository, RaceScoreRepository raceScoreRepository,
            UserCommentsRepository userCommentsRepository) {
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.raceScoreRepository = raceScoreRepository;
        this.userCommentsRepository = userCommentsRepository;
    }

    @MessageMapping("/addcomment")
    @SendTo("/topic/newcomment")
    public Comments addComment(@Valid CommentCreateDTO comment) {
        Optional<User> user = userRepository.findById(comment.getUserId());

        if (!user.isPresent()) {
            return null;
        }

        User exisitingUser = user.get();

        Comments newComment = new Comments();
        newComment.setComment(comment.getComment());
        newComment.setRaceId(comment.getRaceId());
        newComment.setUser(exisitingUser);

        commentsRepository.save(newComment);
        return newComment;
    }

    @MessageMapping("/getComments")
    @SendTo("/topic/allcomments")
    public List<Comments> getComments(GetCommentsDTO commentsDTO) {
        List<Comments> comments = commentsRepository.findAll();
        List<Comments> filteredComments = comments.stream().filter(c -> c.getRaceId().equals(commentsDTO.getRaceId()))
                .toList();

        return filteredComments;
    }

    @MessageMapping("/deletecomment")
    @SendTo("/topic/deletedcomment")
    public CommentDeletedDTO deleteComment(@Valid CommentDeleteDTO commentDelete) {
        CommentDeletedDTO deletedDTO = new CommentDeletedDTO();

        commentsRepository.deleteById(commentDelete.getId());

        deletedDTO.setId(commentDelete.getId());
        return deletedDTO;
    }

    @MessageMapping("/score")
    @SendTo("/topic/scorechanged")
    public ChangedScoreDTO changeScore(@Valid ChangeScoreDTO changeScore) {
        Optional<RaceScore> raceScoreValue = raceScoreRepository.findAll().stream().filter(
                s -> s.getRaceId().equals(changeScore.getRaceId()) && s.getUser().getId() == changeScore.getUserId())
                .findFirst();

        int changeScoreId = raceScoreValue.isPresent() ? raceScoreValue.get().getId() : -1;
        if (changeScoreId > 0) {
            Optional<RaceScore> raceScore = raceScoreRepository.findById(changeScoreId);

            if (raceScore.isPresent()) {
                var raceScoreToModify = raceScore.get();

                if (changeScore.getRating() > 0) {
                    raceScoreToModify.setRating(changeScore.getRating());

                    raceScoreRepository.save(raceScoreToModify);
                }

                return new ChangedScoreDTO(raceScoreToModify.getId(), raceScoreToModify.getRating());
            }
        } else {
            RaceScore raceScore = new RaceScore();
            Optional<User> user = userRepository.findById(changeScore.getUserId());

            raceScore.setRaceId(changeScore.getRaceId());
            raceScore.setRating(changeScore.getRating());
            raceScore.setUser(user.get());

            raceScoreRepository.save(raceScore);
        }

        return new ChangedScoreDTO(changeScore.getId(), changeScore.getRating());
    }

    @MessageMapping("/like")
    @SendTo("/topic/likechanged")
    public GeneralizedWSResponse changeLike(@Valid ChangeLikeDTO likeDTO) {
        int changeLikeId = likeDTO.getId();

        if (changeLikeId > 0) {
            Optional<UserComments> userComment = userCommentsRepository.findById(likeDTO.getId());

            if (userComment.isPresent()) {
                var userCommentToModify = userComment.get();

                userCommentToModify.setCurrentlyLikes(likeDTO.isLikes());

                userCommentsRepository.save(userCommentToModify);
            }
        } else {
            UserComments userComment = new UserComments();
            Optional<User> user = userRepository.findById(likeDTO.getUserId());
            Optional<Comments> comment = commentsRepository.findById(likeDTO.getCommentId());

            userComment.setUser(user.get());
            userComment.setComment(comment.get());
            userComment.setCurrentlyLikes(likeDTO.isLikes());
        }

        return new GeneralizedWSResponse("The like status changed");
    }
}
